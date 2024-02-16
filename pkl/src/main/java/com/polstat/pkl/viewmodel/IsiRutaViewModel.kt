package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.relation.RutaWithKeluarga
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Lokasi
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.ui.event.IsiRutaScreenEvent
import com.polstat.pkl.ui.state.IsiRutaScreenState
import com.polstat.pkl.ui.state.Message
import com.polstat.pkl.utils.Result
import com.polstat.pkl.utils.UtilFunctions
import com.polstat.pkl.utils.location.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class IsiRutaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val keluargaRepository: KeluargaRepository,
    private val getLocationUseCase: GetLocationUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    companion object {
        private const val TAG = "CAPI63_ISI_RUTA_VM"
    }

    private val session = sessionRepository.getActiveSession()

    private val _state = MutableStateFlow(IsiRutaScreenState())

    val state = _state.asStateFlow()

    val idBS = savedStateHandle.get<String>("idBS").orEmpty()

    val kodeKlg = savedStateHandle.get<String>("kodeKlg")

    val kodeRuta = savedStateHandle.get<String>("kodeRuta")

    val isListRuta = savedStateHandle.get<Boolean>("isListRuta")

    val isMonitoring = savedStateHandle.get<Boolean>("isMonitoring")

    private val _lastKeluarga = MutableStateFlow(KeluargaEntity())

    val lastKeluarga = _lastKeluarga.asStateFlow()

    private val _lastKeluargaEgb = MutableStateFlow(KeluargaEntity())

    val lastKeluargaEgb = _lastKeluargaEgb.asStateFlow()

    private val _lastRuta = MutableStateFlow(RutaEntity())

    val lastRuta = _lastRuta.asStateFlow()

    private val _ruta = MutableStateFlow(RutaEntity())

    val ruta = _ruta.asStateFlow()

    private val _keluarga = MutableStateFlow(KeluargaEntity())

    val keluarga = _keluarga.asStateFlow()

    private val _lokasi = MutableStateFlow(Lokasi())

    private val lokasi = _lokasi.asStateFlow()

    private val _isKlgValid = MutableStateFlow(false)

    val isKlgValid = _isKlgValid.asStateFlow()

    private val _isRutaValid = MutableStateFlow(false)

    val isRutaValid = _isRutaValid.asStateFlow()

    private val _klgQueue = MutableStateFlow<MutableList<Keluarga>>(mutableListOf())

    val klgQueue = _klgQueue.asStateFlow()

    var allKodeKlg : List<String> = emptyList()

    init {
        if (kodeKlg == null && kodeRuta == null) {
            viewModelScope.launch {
                val getLastKeluargaTask = async { getLastKeluarga(idBS) }
                val getLastKeluargaEgbTask = async { getLastKeluargaEgb(idBS) }
                val getLastRutaTask = async { getLastRuta(idBS) }

                _lastKeluarga.value = getLastKeluargaTask.await()
                _lastKeluargaEgb.value = getLastKeluargaEgbTask.await()
                _lastRuta.value = getLastRutaTask.await()

                setInitialKlgValue()
            }
        } else {
            viewModelScope.launch {
                if (kodeKlg != null && kodeRuta != null) {
                    Log.d(TAG, "idBS: $idBS")
                    Log.d(TAG, "kodeKlg: $kodeKlg")
                    Log.d(TAG, "kodeRuta: $kodeRuta")
                    if (isListRuta == true) {
                        allKodeKlg = kodeKlg.split(";")
                    }
                    setRutaValueEdit()
                    setKeluargaValueEdit()
                    getRuta(kodeRuta)
                    getKeluarga(kodeKlg)
                    delay(500L)
                    setRutaValueEdit()
                    setKeluargaValueEdit()
                    Log.d(TAG, "allKodeKlg: $allKodeKlg")
                }
            }

        }
    }

    private fun setInitialKlgValue() {
        _state.value = state.value.copy(
            SLS = lastKeluarga.value.banjar,
            noSegmen = lastKeluarga.value.noSegmen,
            noBgFisik = UtilFunctions.convertStringToNumber(lastKeluarga.value.noBgFisik).plus(1).toString(),
            noBgSensus = UtilFunctions.convertStringToNumber(lastKeluarga.value.noBgSensus).plus(1).toString()
        )
        Log.d(TAG, "setInitialKlgValue: ${state.value}")
    }
//    kkOrKrt = when (state.value.listKkOrKrt[indexKlg][indexRuta]) {
//        "Kepala Keluarga (KK) saja" -> "1"
//        "Kepala Rumah Tangga (KRT) saja" -> "2"
//        "KK Sekaligus KRT" -> "3"
//        else -> "0"
//    },
    private fun setRutaValueEdit() {
        _state.value = state.value.copy(
            SLS = keluarga.value.banjar,
            SLSMsg = Message(),
            noBgSensus = keluarga.value.noBgSensus,
            noBgSensusMsg = Message(),
            noBgFisik = keluarga.value.noBgFisik,
            noBgFisikMsg = Message(),
            noSegmen = keluarga.value.noSegmen,
            noSegmenMsg = Message(),
            listNoUrutKlg = listOf(keluarga.value.noUrutKlg),
            listNoUrutKlgMsg = listOf(Message()),
            listNoUrutKlgEgb = listOf(keluarga.value.noUrutKlgEgb),
            listNoUrutKlgEgbMsg = listOf(Message()),
            listNamaKK = listOf(keluarga.value.namaKK),
            listNamaKKMsg = listOf(Message()),
            listAlamat = listOf(keluarga.value.alamat),
            listAlamatMsg = listOf(Message()),
            listIsGenzOrtu = listOf(keluarga.value.isGenzOrtu),
            listIsGenzOrtuMsg = listOf(Message()),
            listPenglMkn = listOf(keluarga.value.penglMkn),
            listPenglMknMsg = listOf(Message()),
            listNoUrutRuta = listOf(listOf(ruta.value.noUrutRuta)),
            listNoUrutRutaMsg = listOf(listOf(Message())),
            listKkOrKrt = listOf(listOf(
                    when (ruta.value.kkOrKrt) {
                        "1" -> "Kepala Keluarga (KK) saja"
                        "2" -> "Kepala Rumah Tangga (KRT) saja"
                        "3" -> "KK Sekaligus KRT"
                        else -> "N/A"
                    }
                )
            ),
            listKkOrKrtMsg = listOf(listOf(Message())),
            listNamaKrt = listOf(listOf(ruta.value.namaKrt)),
            listNamaKrtMsg = listOf(listOf(Message())),
            listJmlGenzAnak = listOf(listOf(ruta.value.jmlGenzAnak)),
            listJmlGenzAnakMsg = listOf(listOf(Message())),
            listJmlGenzDewasa = listOf(listOf(ruta.value.jmlGenzDewasa)),
            listJmlGenzDewasaMsg = listOf(listOf(Message())),
            listKatGenz = listOf(listOf(ruta.value.katGenz)),
            listKatGenzMsg = listOf(listOf(Message())),
            listCatatan = listOf(listOf(ruta.value.catatan))
        )
        Log.d(TAG, "setInitialRutaValueEdit: ${state.value}")
    }

    private fun setKeluargaValueEdit() {
        _state.value = state.value.copy(
            SLS = keluarga.value.banjar,
            SLSMsg = Message(),
            noBgSensus = keluarga.value.noBgSensus,
            noBgSensusMsg = Message(),
            noBgFisik = keluarga.value.noBgFisik,
            noBgFisikMsg = Message(),
            noSegmen = keluarga.value.noSegmen,
            noSegmenMsg = Message(),
            listNoUrutKlg = listOf(keluarga.value.noUrutKlg),
            listNoUrutKlgMsg = listOf(Message()),
            listNoUrutKlgEgb = listOf(keluarga.value.noUrutKlgEgb),
            listNoUrutKlgEgbMsg = listOf(Message()),
            listNamaKK = listOf(keluarga.value.namaKK),
            listNamaKKMsg = listOf(Message()),
            listAlamat = listOf(keluarga.value.alamat),
            listAlamatMsg = listOf(Message()),
            listIsGenzOrtu = listOf(keluarga.value.isGenzOrtu),
            listIsGenzOrtuMsg = listOf(Message()),
            listPenglMkn = listOf(keluarga.value.penglMkn),
            listPenglMknMsg = listOf(Message())
        )
        Log.d(TAG, "setInitialKlgValueEdit: ${state.value}")
    }

    private fun getKeluarga(
        kodeKlg: String
    ) {
        viewModelScope.launch {
            keluargaRepository.getKeluarga(kodeKlg).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            Log.d(TAG, "Error getKeluarga: $error")
                        }
                    }
                    is Result.Loading -> Log.d(TAG, "getKeluarga: Loading...")
                    is Result.Success -> {
                        result.data?.let { klg ->
                            _keluarga.value = klg
                            Log.d(TAG, "getKeluarga: Berhasil mendapatkan keluarga ${keluarga.value}")
                        }
                    }
                }
            }
        }
    }

    private fun insertRuta(
        ruta: Ruta
    ){
        viewModelScope.launch {
            localRutaRepository.insertRuta(ruta, LocalRutaRepository.Method.Insert).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    private fun updateRuta(
        ruta: Ruta
    ){
        viewModelScope.launch {
            localRutaRepository.updateRuta(ruta).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    private fun insertKeluarga(
        keluarga: Keluarga
    ) {
        viewModelScope.launch {
            keluargaRepository.insertKeluarga(keluarga, KeluargaRepository.Method.Insert)
                .collectLatest { message ->
                    Log.d(TAG, message)
            }
        }
    }

    private fun updateKeluarga(
        keluarga: Keluarga
    ) {
        viewModelScope.launch {
            keluargaRepository.updateKeluarga(keluarga)
                .collectLatest { message ->
                    Log.d(TAG, message)
                }
        }
    }

    private fun insertKeluargaAndRuta(
        kodeKlg: String,
        kodeRuta: String
    ) {
        viewModelScope.launch {
            localRutaRepository.insertKeluargaAndRuta(kodeKlg, kodeRuta).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    private suspend fun updateStatusListKeluarga(listKodeKlg: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            listKodeKlg.forEach { kodeKlg ->
                keluargaRepository.updateStatusKeluarga(kodeKlg).collectLatest { message ->
                    Log.d(TAG, message)
                }
            }
        }
    }

    fun getRutaLocation() {
        viewModelScope.launch {
            getLocationUseCase.invoke().collect { location ->
                if (location != null) {
                    _lokasi.value = location
                }
                Log.d(TAG, "getRutaLocation: $location")
            }
        }
    }

    private suspend fun getLastKeluarga(idBS: String): KeluargaEntity {
        var keluargaEntity = KeluargaEntity(
            kodeKlg = "",
            banjar = "",
            noBgFisik = "0",
            noBgSensus = "0",
            noSegmen = "S000",
            noUrutKlg = "0",
            noUrutKlgEgb = 0,
            namaKK = "",
            alamat = "",
            isGenzOrtu = 0,
            penglMkn = 0,
            idBS = "",
            nimPencacah = "",
            status = ""
        )

        keluargaRepository.getLastKeluarga(idBS).collectLatest { result ->
            when (result) {
                is Result.Success -> {
                    result.data?.let { keluarga ->
                        keluargaEntity = keluarga
                        Log.d(TAG, "getLastKeluarga: Berhasil mendapatkan last keluarga $keluargaEntity")
                    }
                }
                is Result.Loading -> Log.d(TAG, "getLastKeluarga: Loading...")
                is Result.Error -> {
                    result.message?.let { error ->
                        Log.d(TAG, "Error getLastKeluarga: $error")
                    }
                }
            }
        }
        return keluargaEntity
    }

    private suspend fun getLastKeluargaEgb(idBS: String): KeluargaEntity {
        var keluargaEntity = KeluargaEntity(
            kodeKlg = "",
            banjar = "",
            noBgFisik = "0",
            noBgSensus = "0",
            noSegmen = "S000",
            noUrutKlg = "0",
            noUrutKlgEgb = 0,
            namaKK = "",
            alamat = "",
            isGenzOrtu = 0,
            penglMkn = 0,
            idBS = "",
            nimPencacah = "",
            status = ""
        )

        keluargaRepository.getLastKeluargaEgb(idBS).collectLatest { result ->
            when (result) {
                is Result.Success -> {
                    result.data?.let { keluarga ->
                        keluargaEntity = keluarga
                        Log.d(TAG, "getLastKeluargaEgb: Berhasil mendapatkan last keluarga egb $keluargaEntity")
                    }
                }
                is Result.Loading -> Log.d(TAG, "getLastKeluargaEgb: Loading...")
                is Result.Error -> {
                    result.message?.let { error ->
                        Log.d(TAG, "Error getLastKeluargaEgb: $error")
                    }
                }
            }
        }
        return keluargaEntity
    }

    private suspend fun getLastRuta(idBS: String): RutaEntity {
        var rutaEntity = RutaEntity(
            kodeRuta = "",
            noUrutRuta = "0",
            noUrutEgb = 0,
            kkOrKrt = "",
            namaKrt = "",
            jmlGenzAnak = 0,
            jmlGenzDewasa = 0,
            katGenz = 0,
            longitude = 0.0,
            latitude = 0.0,
            catatan = "",
            idBS = "",
            nimPencacah = "",
            status = ""
        )

        localRutaRepository.getLastRuta(idBS).collectLatest { result ->
            when (result) {
                is Result.Success -> {
                    result.data?.let { ruta ->
                        rutaEntity = ruta
                        Log.d(TAG, "getLastRuta: Berhasil mendapatkan last ruta $rutaEntity")
                    }
                }
                is Result.Loading -> Log.d(TAG, "getLastRuta: Loading...")
                is Result.Error -> {
                    result.message?.let { error ->
                        Log.d(TAG, "Error getLastRuta: $error")
                    }
                }
            }
        }
        return rutaEntity
    }

    fun getRuta(kodeRuta: String) {
        viewModelScope.launch {
            localRutaRepository.getRuta(kodeRuta).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let {
                            _ruta.value = it
                        }
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            Log.d(TAG, "Error getRuta: $error")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getRuta: Loading...")
                    }
                }
            }
        }
    }

    suspend fun onEvent(
        event: IsiRutaScreenEvent,
        index: Int = 0,
        index2: Int = 0
    ) {
        when(event) {
            is IsiRutaScreenEvent.SLSChanged -> {
                _state.emit(state.value.copy(SLS = event.sls))
            }
            is IsiRutaScreenEvent.NoSegmenChanged -> {
                _state.emit(state.value.copy(noSegmen = event.noSegmen))
                if (!UtilFunctions.isValidNoSegmenFormat(state.value.noSegmen)) {
                    _state.emit(state.value.copy(noSegmenMsg = Message(null, "Error: No segmen tidak valid! (Format No segmen: Sxxx)")))
                } else {
                    _state.emit(state.value.copy(noSegmenMsg = Message()))
                }
            }
            is IsiRutaScreenEvent.NoBgFisikChanged -> {
                _state.emit(state.value.copy(noBgFisik = event.noBgFisik))
                if (state.value.noBgSensus < state.value.noBgFisik) {
                    _state.emit(state.value.copy(noBgSensusMsg = Message(null, "Error: Bangunan sensus harus >= bangunan fisik!")))
                } else {
                    _state.emit(state.value.copy(noBgSensusMsg = Message()))
                }
            }
            is IsiRutaScreenEvent.NoBgSensusChanged -> {
                _state.emit(state.value.copy(noBgSensus = event.noBgSensus))
                if (state.value.noBgSensus < state.value.noBgFisik) {
                    _state.emit(state.value.copy(noBgSensusMsg = Message(null, "Error: Bangunan sensus harus >= bangunan fisik!")))
                } else {
                    _state.emit(state.value.copy(noBgSensusMsg = Message()))
                }
            }
            is IsiRutaScreenEvent.JmlKlgChanged -> {
                val newSize = event.jmlKlg
                val oldSize = state.value.listNoUrutKlg.size

                val diff = newSize - oldSize

                val updatedState = when {
                    state.value.listNoUrutKlg.isEmpty() || diff > 0 -> {
                        val newElements = List(event.jmlKlg) { UtilFunctions.convertStringToNumber(lastKeluarga.value.noUrutKlg) + it + 1 }.map { it.toString() }
                        state.value.copy(
                            listNoUrutKlg = newElements,
                            listNamaKK = state.value.listNamaKK + List(diff) { "" },
                            listAlamat = state.value.listAlamat + List(diff) { "" },
                            listIsGenzOrtu = state.value.listIsGenzOrtu + List(diff) { 0 },
                            listNoUrutKlgEgb = state.value.listNoUrutKlgEgb + List(diff) { 0 },
                            listPenglMkn = state.value.listPenglMkn + List(diff) { 0 },
                            listNoUrutRuta = state.value.listNoUrutRuta + List(diff) { emptyList() },
                            listKkOrKrt = state.value.listKkOrKrt + List(diff) { emptyList() },
                            listNamaKrt = state.value.listNamaKrt + List(diff) { emptyList() },
                            listJmlGenzAnak = state.value.listJmlGenzAnak + List(diff) { emptyList() },
                            listJmlGenzDewasa = state.value.listJmlGenzDewasa + List(diff) { emptyList() },
                            listKatGenz = state.value.listKatGenz + List(diff) { emptyList() },
                            listLong = state.value.listLong + List(diff) { emptyList() },
                            listLat = state.value.listLat + List(diff) { emptyList() },
                            listCatatan = state.value.listCatatan + List(diff) { emptyList() },
                            listNoUrutKlgMsg = state.value.listNoUrutKlgMsg + List(diff) { Message() },
                            listNamaKKMsg = state.value.listNamaKKMsg + List(diff) { Message() },
                            listAlamatMsg = state.value.listAlamatMsg + List(diff) { Message() },
                            listIsGenzOrtuMsg = state.value.listIsGenzOrtuMsg + List(diff) { Message() },
                            listNoUrutKlgEgbMsg = state.value.listNoUrutKlgEgbMsg + List(diff) { Message() },
                            listPenglMknMsg = state.value.listPenglMknMsg + List(diff) { Message() },
                            listNoUrutRutaMsg = state.value.listNoUrutRutaMsg + List(diff) { emptyList() },
                            listKkOrKrtMsg = state.value.listKkOrKrtMsg + List(diff) { emptyList() },
                            listNamaKrtMsg = state.value.listNamaKrtMsg + List(diff) { emptyList() },
                            listJmlGenzAnakMsg = state.value.listJmlGenzAnakMsg + List(diff) { emptyList() },
                            listJmlGenzDewasaMsg = state.value.listJmlGenzDewasaMsg + List(diff) { emptyList() },
                            listKatGenzMsg = state.value.listKatGenzMsg + List(diff) { emptyList() }
                        )
                    }

                    diff < 0 -> {
                        state.value.copy(
                            listNoUrutKlg = state.value.listNoUrutKlg.take(newSize),
                            listNamaKK = state.value.listNamaKK.take(newSize),
                            listAlamat = state.value.listAlamat.take(newSize),
                            listIsGenzOrtu = state.value.listIsGenzOrtu.take(newSize),
                            listNoUrutKlgEgb = state.value.listNoUrutKlgEgb.take(newSize),
                            listPenglMkn = state.value.listPenglMkn.take(newSize),
                            listNoUrutRuta = state.value.listNoUrutRuta.take(newSize),
                            listKkOrKrt = state.value.listKkOrKrt.take(newSize),
                            listNamaKrt = state.value.listNamaKrt.take(newSize),
                            listJmlGenzAnak = state.value.listJmlGenzAnak.take(newSize),
                            listJmlGenzDewasa = state.value.listJmlGenzDewasa.take(newSize),
                            listKatGenz = state.value.listKatGenz.take(newSize),
                            listLong = state.value.listLong.take(newSize),
                            listLat = state.value.listLat.take(newSize),
                            listCatatan = state.value.listCatatan.take(newSize),
                            listNoUrutKlgMsg = state.value.listNoUrutKlgMsg.take(newSize),
                            listNamaKKMsg = state.value.listNamaKKMsg.take(newSize),
                            listAlamatMsg = state.value.listAlamatMsg.take(newSize),
                            listIsGenzOrtuMsg = state.value.listIsGenzOrtuMsg.take(newSize),
                            listNoUrutKlgEgbMsg = state.value.listNoUrutKlgEgbMsg.take(newSize),
                            listPenglMknMsg = state.value.listPenglMknMsg.take(newSize),
                            listNoUrutRutaMsg = state.value.listNoUrutRutaMsg.take(newSize),
                            listKkOrKrtMsg = state.value.listKkOrKrtMsg.take(newSize),
                            listNamaKrtMsg = state.value.listNamaKrtMsg.take(newSize),
                            listJmlGenzAnakMsg = state.value.listJmlGenzAnakMsg.take(newSize),
                            listJmlGenzDewasaMsg = state.value.listJmlGenzDewasaMsg.take(newSize),
                            listKatGenzMsg = state.value.listKatGenzMsg.take(newSize)
                        )
                    }

                    else -> state.value
                }

                _state.emit(updatedState)

                _state.emit(state.value.copy(jmlKlg = event.jmlKlg))

                Log.d(TAG, "onEvent: listNoUrutKlg ${state.value.listNoUrutKlg}")
                Log.d(TAG, "onEvent: listNamaKK ${state.value.listNamaKK}")
                Log.d(TAG, "onEvent: listAlamat ${state.value.listAlamat}")
                Log.d(TAG, "onEvent: listIsGenzOrtu ${state.value.listIsGenzOrtu}")
                Log.d(TAG, "onEvent: listNoUrutKlgEgb ${state.value.listNoUrutKlgEgb}")
                Log.d(TAG, "onEvent: listPenglMkn ${state.value.listPenglMkn}")
                Log.d(TAG, "onEvent: listNoUrutRuta ${state.value.listNoUrutRuta}")
                Log.d(TAG, "onEvent: listKkOrKrt ${state.value.listKkOrKrt}")
                Log.d(TAG, "onEvent: listJmlGenzAnak ${state.value.listJmlGenzAnak}")
                Log.d(TAG, "onEvent: listJmlGenzDewasa ${state.value.listJmlGenzDewasa}")
                Log.d(TAG, "onEvent: listKatGenz ${state.value.listKatGenz}")
                Log.d(TAG, "onEvent: listLong ${state.value.listLong}")
                Log.d(TAG, "onEvent: listLat ${state.value.listLat}")

            }
            is IsiRutaScreenEvent.NoUrutKlgChanged -> {
                val newListNoUrutKlg = state.value.listNoUrutKlg.toMutableList()
                newListNoUrutKlg[index] = event.noUrutKlg
                _state.emit(state.value.copy(listNoUrutKlg = newListNoUrutKlg))
                if (UtilFunctions.convertStringToNumber(state.value.listNoUrutKlg[index]) != UtilFunctions.convertStringToNumber(lastKeluarga.value.noUrutKlg).plus(1)) {
                    val newListNoUrutKlgMsg = state.value.listNoUrutKlgMsg.toMutableList()
                    newListNoUrutKlgMsg[index] = Message(warning = "Warning: Nomor urut keluarga terakhir = ${lastKeluarga.value.noUrutKlg}")
                    _state.emit(state.value.copy(listNoUrutKlgMsg = newListNoUrutKlgMsg))
                } else {
                    val newListNoUrutKlgMsg = state.value.listNoUrutKlgMsg.toMutableList()
                    newListNoUrutKlgMsg[index] = Message()
                    _state.emit(state.value.copy(listNoUrutKlgMsg = newListNoUrutKlgMsg))
                }
            }
            is IsiRutaScreenEvent.NamaKKChanged -> {
                val newListNamaKK = state.value.listNamaKK.toMutableList()
                newListNamaKK[index] = event.namaKK
                _state.emit(state.value.copy(listNamaKK = newListNamaKK))
            }
            is IsiRutaScreenEvent.AlamatChanged -> {
                val newListAlamat = state.value.listAlamat.toMutableList()
                newListAlamat[index] = event.alamat
                _state.emit(state.value.copy(listAlamat = newListAlamat))
            }
            is IsiRutaScreenEvent.IsGenzOrtuChanged -> {
                val newListIsGenzOrtu = state.value.listIsGenzOrtu.toMutableList()
                newListIsGenzOrtu[index] = event.isGenzOrtu

                _state.emit(state.value.copy(listIsGenzOrtu = newListIsGenzOrtu))

                val newListNoUrutKlgEgb = state.value.listNoUrutKlgEgb.toMutableList()

                if (newListIsGenzOrtu[index] > 0) {
                    val maxElement = newListNoUrutKlgEgb.maxOrNull()
                    if (maxElement == 0) {
                        newListNoUrutKlgEgb[index] = lastKeluargaEgb.value.noUrutKlgEgb + 1
                    } else if (maxElement != 0 && newListNoUrutKlgEgb[index] == 0) {
                        newListNoUrutKlgEgb[index] = maxElement?.plus(1) ?: 0
                    }
                } else {
                    newListNoUrutKlgEgb[index] = 0
                }
                _state.emit(state.value.copy(listNoUrutKlgEgb = newListNoUrutKlgEgb))
                Log.d(TAG, "onEvent: listNoUrutKlgEgb ${state.value.listNoUrutKlgEgb}")
            }
            is IsiRutaScreenEvent.NoUrutKlgEgbChanged -> {
                val newListNoUrutKlgEgb = state.value.listNoUrutKlgEgb.toMutableList()
                newListNoUrutKlgEgb[index] = event.noUrutKlgEgb
                _state.emit(state.value.copy(listNoUrutKlgEgb = newListNoUrutKlgEgb))
                if (state.value.listNoUrutKlgEgb[index] != lastKeluargaEgb.value.noUrutKlgEgb.plus(1)) {
                    val newListNoUrutKlgEgbMsg = state.value.listNoUrutKlgEgbMsg.toMutableList()
                    newListNoUrutKlgEgbMsg[index] = Message(warning = "Warning: Nomor urut keluarga eligible terakhir = ${lastKeluargaEgb.value.noUrutKlgEgb}")
                    _state.emit(state.value.copy(listNoUrutKlgEgbMsg = newListNoUrutKlgEgbMsg))
                } else {
                    val newListNoUrutKlgEgbMsg = state.value.listNoUrutKlgEgbMsg.toMutableList()
                    newListNoUrutKlgEgbMsg[index] = Message()
                    _state.emit(state.value.copy(listNoUrutKlgEgbMsg = newListNoUrutKlgEgbMsg))
                }
            }
            is IsiRutaScreenEvent.PenglMknChanged -> {
                val newSize = event.penglMkn
                val oldSize = state.value.listNoUrutRuta[index].size
                Log.d(TAG, "onEvent: newSize = $newSize, oldSize = $oldSize, list no urut ruta = ${state.value.listNoUrutRuta}")

                val diff = newSize - oldSize

                val updatedState = when {
                    state.value.listNoUrutRuta[index].isEmpty() || diff > 0 -> {
                        val newListNoUrutRuta = state.value.listNoUrutRuta.toMutableList()
                        newListNoUrutRuta[index] = if (index == 0) {
                            (UtilFunctions.convertStringToNumber(lastRuta.value.noUrutRuta) + 1..UtilFunctions.convertStringToNumber(lastRuta.value.noUrutRuta) + newSize).toList().map { it.toString() }
                        } else {
                            var beforeIndex = index - 1
                            while(newListNoUrutRuta[beforeIndex].isEmpty()) {
                                if (beforeIndex == 0) {
                                    break
                                }
                                beforeIndex--
                            }
                            if (beforeIndex == 0 && newListNoUrutRuta[beforeIndex].isEmpty()) {
                                (UtilFunctions.convertStringToNumber(lastRuta.value.noUrutRuta) + 1..UtilFunctions.convertStringToNumber(lastRuta.value.noUrutRuta) + newSize).toList().map { it.toString() }
                            } else {
                                ((newListNoUrutRuta[beforeIndex].map { UtilFunctions.convertStringToNumber(it) }.max().plus(1))..(newListNoUrutRuta[beforeIndex].map { UtilFunctions.convertStringToNumber(it) }.max().plus(newSize))).toList().map { it.toString() }
                            }
                        }

                        val newListKkOrKrt = state.value.listKkOrKrt.toMutableList()
                        newListKkOrKrt[index] = state.value.listKkOrKrt[index] + List(diff) { "" }

                        val newListNamaKrt = state.value.listNamaKrt.toMutableList()
                        newListNamaKrt[index] = state.value.listNamaKrt[index] + List(diff) { "" }

                        val newListJmlGenzAnak = state.value.listJmlGenzAnak.toMutableList()
                        newListJmlGenzAnak[index] = state.value.listJmlGenzAnak[index] + List(diff) { 0 }

                        val newListJmlGenzDewasa = state.value.listJmlGenzDewasa.toMutableList()
                        newListJmlGenzDewasa[index] = state.value.listJmlGenzDewasa[index] + List(diff) { 0 }

                        val newListKatGenz = state.value.listKatGenz.toMutableList()
                        newListKatGenz[index] = state.value.listKatGenz[index] + List(diff) { 0 }

                        val newListLong = state.value.listLong.toMutableList()
                        newListLong[index] = state.value.listLong[index] + List(diff) { 0.0 }

                        val newListLat = state.value.listLat.toMutableList()
                        newListLat[index] = state.value.listLat[index] + List(diff) { 0.0 }

                        val newListCatatan = state.value.listCatatan.toMutableList()
                        newListCatatan[index] = state.value.listCatatan[index] + List(diff) { "" }

                        val newListNoUrutRutaMsg = state.value.listNoUrutRutaMsg.toMutableList()
                        newListNoUrutRutaMsg[index] = state.value.listNoUrutRutaMsg[index] + List(diff) { Message() }

                        val newListKkOrKrtMsg = state.value.listKkOrKrtMsg.toMutableList()
                        newListKkOrKrtMsg[index] = state.value.listKkOrKrtMsg[index] + List(diff) { Message() }

                        val newListNamaKrtMsg = state.value.listNamaKrtMsg.toMutableList()
                        newListNamaKrtMsg[index] = state.value.listNamaKrtMsg[index] + List(diff) { Message() }

                        val newListJmlGenzAnakMsg = state.value.listJmlGenzAnakMsg.toMutableList()
                        newListJmlGenzAnakMsg[index] = state.value.listJmlGenzAnakMsg[index] + List(diff) { Message() }

                        val newListJmlGenzDewasaMsg = state.value.listJmlGenzDewasaMsg.toMutableList()
                        newListJmlGenzDewasaMsg[index] = state.value.listJmlGenzDewasaMsg[index] + List(diff) { Message() }

                        val newListKatGenzMsg = state.value.listKatGenzMsg.toMutableList()
                        newListKatGenzMsg[index] = state.value.listKatGenzMsg[index] + List(diff) { Message() }


                        state.value.copy(
                            listNoUrutRuta = newListNoUrutRuta,
                            listKkOrKrt = newListKkOrKrt,
                            listNamaKrt = newListNamaKrt,
                            listJmlGenzAnak = newListJmlGenzAnak,
                            listJmlGenzDewasa = newListJmlGenzDewasa,
                            listKatGenz = newListKatGenz,
                            listLong = newListLong,
                            listLat = newListLat,
                            listCatatan = newListCatatan,
                            listNoUrutRutaMsg = newListNoUrutRutaMsg,
                            listKkOrKrtMsg = newListKkOrKrtMsg,
                            listNamaKrtMsg = newListNamaKrtMsg,
                            listJmlGenzAnakMsg = newListJmlGenzAnakMsg,
                            listJmlGenzDewasaMsg = newListJmlGenzDewasaMsg,
                            listKatGenzMsg = newListKatGenzMsg,
                        )
                    }

                    diff < 0 -> {
                        val newListNoUrutRuta = updateListAtIndex(state.value.listNoUrutRuta, index, newSize)
                        val newListKkOrKrt = updateListAtIndex(state.value.listKkOrKrt, index, newSize)
                        val newListNamaKrt = updateListAtIndex(state.value.listNamaKrt, index, newSize)
                        val newListJmlGenzAnak = updateListAtIndex(state.value.listJmlGenzAnak, index, newSize)
                        val newListJmlGenzDewasa = updateListAtIndex(state.value.listJmlGenzDewasa, index, newSize)
                        val newListKatGenz = updateListAtIndex(state.value.listKatGenz, index, newSize)
                        val newListLong = updateListAtIndex(state.value.listLong, index, newSize)
                        val newListLat = updateListAtIndex(state.value.listLat, index, newSize)
                        val newListCatatan = updateListAtIndex(state.value.listCatatan, index, newSize)
                        val newListNoUrutRutaMsg = updateListAtIndex(state.value.listNoUrutRutaMsg, index, newSize)
                        val newListKkOrKrtMsg = updateListAtIndex(state.value.listKkOrKrtMsg, index, newSize)
                        val newListNamaKrtMsg = updateListAtIndex(state.value.listNamaKrtMsg, index, newSize)
                        val newListJmlGenzAnakMsg = updateListAtIndex(state.value.listJmlGenzAnakMsg, index, newSize)
                        val newListJmlGenzDewasaMsg = updateListAtIndex(state.value.listJmlGenzDewasaMsg, index, newSize)
                        val newListKatGenzMsg = updateListAtIndex(state.value.listKatGenzMsg, index, newSize)

                        state.value.copy(
                            listNoUrutRuta = newListNoUrutRuta,
                            listKkOrKrt = newListKkOrKrt,
                            listNamaKrt = newListNamaKrt,
                            listJmlGenzAnak = newListJmlGenzAnak,
                            listJmlGenzDewasa = newListJmlGenzDewasa,
                            listKatGenz = newListKatGenz,
                            listLong = newListLong,
                            listLat = newListLat,
                            listCatatan = newListCatatan,
                            listNoUrutRutaMsg = newListNoUrutRutaMsg,
                            listKkOrKrtMsg = newListKkOrKrtMsg,
                            listNamaKrtMsg = newListNamaKrtMsg,
                            listJmlGenzAnakMsg = newListJmlGenzAnakMsg,
                            listJmlGenzDewasaMsg = newListJmlGenzDewasaMsg,
                            listKatGenzMsg = newListKatGenzMsg,
                        )
                    }

                    else -> state.value
                }

                _state.emit(updatedState)

                val newListPenglMkn = state.value.listPenglMkn.toMutableList()
                newListPenglMkn[index] = event.penglMkn

                _state.emit(state.value.copy(listPenglMkn = newListPenglMkn))

                Log.d(TAG, "onEvent: listNoUrutRuta ${state.value.listNoUrutRuta}, indexKlg: $index, indexRuta: $index2")
                Log.d(TAG, "onEvent: listKkOrKrt ${state.value.listKkOrKrt}, indexKlg: $index, indexRuta: $index2")
                Log.d(TAG, "onEvent: listJmlGenzAnak ${state.value.listJmlGenzAnak}, indexKlg: $index, indexRuta: $index2")
                Log.d(TAG, "onEvent: listJmlGenzDewasa ${state.value.listJmlGenzDewasa}, indexKlg: $index, indexRuta: $index2")
                Log.d(TAG, "onEvent: listKatGenz ${state.value.listKatGenz}, indexKlg: $index, indexRuta: $index2")
                Log.d(TAG, "onEvent: listLong ${state.value.listLong}, indexKlg: $index, indexRuta: $index2")
                Log.d(TAG, "onEvent: listLat ${state.value.listLat}, indexKlg: $index, indexRuta: $index2")

            }
            is IsiRutaScreenEvent.NoUrutRutaChanged -> {
                val newListNoUrutRuta = state.value.listNoUrutRuta.toMutableList()
                newListNoUrutRuta[index] = newListNoUrutRuta[index].toMutableList().apply {
                    set(index2, event.noUrutRuta)
                }
                _state.emit(state.value.copy(listNoUrutRuta = newListNoUrutRuta))
                if (UtilFunctions.convertStringToNumber(state.value.listNoUrutRuta[index][index2]) != UtilFunctions.convertStringToNumber(lastRuta.value.noUrutRuta).plus(1)) {
                    val newListNoUrutRutaMsg = state.value.listNoUrutRutaMsg.map { it.toMutableList() }.toMutableList()
                    newListNoUrutRutaMsg[index][index2] = Message(warning = "Warning: Nomor urut ruta terakhir = ${lastRuta.value.noUrutRuta}")
                    _state.emit(state.value.copy(listNoUrutRutaMsg = newListNoUrutRutaMsg))
                } else {
                    val newListNoUrutRutaMsg = state.value.listNoUrutRutaMsg.map { it.toMutableList() }.toMutableList()
                    newListNoUrutRutaMsg[index][index2] = Message()
                    _state.emit(state.value.copy(listNoUrutRutaMsg = newListNoUrutRutaMsg))
                }
            }
            is IsiRutaScreenEvent.KKOrKRTChanged -> {
                val newListKkOrKrt = state.value.listKkOrKrt.toMutableList()
                newListKkOrKrt[index] = newListKkOrKrt[index].toMutableList().apply {
                    set(index2, event.kkOrKRT)
                }
                _state.emit(state.value.copy(listKkOrKrt = newListKkOrKrt))
            }
            is IsiRutaScreenEvent.NamaKRTChanged -> {
                val newListNamaKrt = state.value.listNamaKrt.toMutableList()
                newListNamaKrt[index] = newListNamaKrt[index].toMutableList().apply {
                    set(index2, event.namaKRT)
                }
                _state.emit(state.value.copy(listNamaKrt = newListNamaKrt))

                if ((state.value.listKkOrKrt[index][index2] == "KK Sekaligus KRT")  && (state.value.listNamaKrt[index][index2] != state.value.listNamaKK[index])) {
                    val newListNamaKrtMsg = state.value.listNamaKrtMsg.map { it.toMutableList() }.toMutableList()
                    newListNamaKrtMsg[index][index2] = Message(warning = "Warning: Nama kepala rumah tangga berbeda dengan nama kepala keluarga")
                    _state.emit(state.value.copy(listNamaKrtMsg = newListNamaKrtMsg))
                } else {
                    val newListNamaKrtMsg = state.value.listNamaKrtMsg.map { it.toMutableList() }.toMutableList()
                    newListNamaKrtMsg[index][index2] = Message()
                    _state.emit(state.value.copy(listNamaKrtMsg = newListNamaKrtMsg))
                }

            }
            is IsiRutaScreenEvent.JmlGenzAnakChanged -> {
                val newListJmlGenzAnak = state.value.listJmlGenzAnak.toMutableList()
                newListJmlGenzAnak[index] = newListJmlGenzAnak[index].toMutableList().apply {
                    set(index2, event.jmlGenzAnak)
                }
                _state.emit(state.value.copy(listJmlGenzAnak = newListJmlGenzAnak))
            }
            is IsiRutaScreenEvent.JmlGenzDewasaChanged -> {
                val newListJmlGenzDewasa = state.value.listJmlGenzDewasa.toMutableList()
                newListJmlGenzDewasa[index] = newListJmlGenzDewasa[index].toMutableList().apply {
                    set(index2, event.jmlGenzDewasa)
                }
                _state.emit(state.value.copy(listJmlGenzDewasa = newListJmlGenzDewasa))
            }
            is IsiRutaScreenEvent.KatGenzChanged -> {
                val newListKatGenz = state.value.listKatGenz.toMutableList()
                newListKatGenz[index] = newListKatGenz[index].toMutableList().apply {
                    set(index2, event.katGenz)
                }
                _state.emit(state.value.copy(listKatGenz = newListKatGenz))
            }

            is IsiRutaScreenEvent.CatatanChanged -> {
                val newListCatatan = state.value.listCatatan.toMutableList()
                newListCatatan[index] = newListCatatan[index].toMutableList().apply {
                    set(index2, event.catatan)
                }
                _state.emit(state.value.copy(listCatatan = newListCatatan))
            }

            is IsiRutaScreenEvent.submit -> {

                if (state.value.jmlKlg == 0) {
                    //Masih proses
                } else {
                    repeat(state.value.jmlKlg) { indexKlg ->
                        val kodeKlg = if (state.value.listNoUrutKlg[indexKlg] == "") {
                            "K${idBS}${state.value.noSegmen}${generateRandomDigitString( 3)}"
                        } else {
                            "K${idBS}${state.value.noSegmen}${UtilFunctions.padWithZeros(state.value.listNoUrutKlg[indexKlg], 3)}"
                        }
                        var keluarga = Keluarga(
                            SLS = state.value.SLS,
                            noSegmen = state.value.noSegmen,
                            noBgFisik = state.value.noBgFisik,
                            noBgSensus = state.value.noBgSensus,
                            noUrutKlg = state.value.listNoUrutKlg[indexKlg],
                            namaKK = state.value.listNamaKK[indexKlg],
                            alamat = state.value.listAlamat[indexKlg],
                            isGenzOrtu = state.value.listIsGenzOrtu[indexKlg],
                            noUrutKlgEgb = state.value.listNoUrutKlgEgb[indexKlg],
                            penglMkn = state.value.listPenglMkn[indexKlg],
                            idBS = idBS,
                            kodeKlg = kodeKlg,
                            nimPencacah = session?.nim ?: "",
                            status = "insert"
                        )
                        Log.d(TAG, "onEvent: Keluarga Insert $keluarga")

                        validateKlg(keluarga, indexKlg)

                        Log.d(TAG, "onEvent: isKlgValid.value=${isKlgValid.value}")
//                        if (isKlgValid.value) {
//                            insertKeluarga(keluarga)
//                        }
//                        _klgQueue.value.add(indexKlg, keluarga)

                        repeat(state.value.listPenglMkn[indexKlg]) { indexRuta ->
                            val ruta = Ruta(
                                kodeRuta = "R${idBS}${state.value.noSegmen}${UtilFunctions.padWithZeros(state.value.listNoUrutRuta[indexKlg][indexRuta], 3)}",
                                noUrutRuta = state.value.listNoUrutRuta[indexKlg][indexRuta],
                                noUrutEgb = 0,
                                kkOrKrt = when (state.value.listKkOrKrt[indexKlg][indexRuta]) {
                                    "Kepala Keluarga (KK) saja" -> "1"
                                    "Kepala Rumah Tangga (KRT) saja" -> "2"
                                    "KK Sekaligus KRT" -> "3"
                                    else -> "0"
                                },
                                namaKrt = state.value.listNamaKrt[indexKlg][indexRuta],
                                jmlGenzAnak = state.value.listJmlGenzAnak[indexKlg][indexRuta],
                                jmlGenzDewasa = state.value.listJmlGenzDewasa[indexKlg][indexRuta],
                                katGenz = when {
                                    state.value.listJmlGenzAnak[indexKlg][indexRuta] > 0 && state.value.listJmlGenzDewasa[indexKlg][indexRuta] == 0 -> 1
                                    state.value.listJmlGenzAnak[indexKlg][indexRuta] == 0 && state.value.listJmlGenzDewasa[indexKlg][indexRuta] > 0 -> 2
                                    state.value.listJmlGenzAnak[indexKlg][indexRuta] > 0 && state.value.listJmlGenzDewasa[indexKlg][indexRuta] > 0 -> 3
                                    else -> 0
                                },
                                long = lokasi.value.longitude,
                                lat = lokasi.value.latitude,
                                catatan = state.value.listCatatan[indexKlg][indexRuta],
                                idBS = idBS,
                                nimPencacah = session?.nim ?: "",
                                noSegmen = state.value.noSegmen,
                                status = "insert"
                            )
                            Log.d(TAG, "onEvent: Ruta Insert $ruta")

                            validateRuta(ruta, indexKlg, indexRuta)

                            if (isRutaValid.value) {
                                var listRuta = keluarga.ruta + ruta
                                keluarga = keluarga.copy(ruta = listRuta)
                                Log.d(TAG, "onEvent: $keluarga")
                            }

                        }

                        if (isKlgValid.value) {
                            _klgQueue.value.add(keluarga)
                            Log.d(TAG, "onEvent: klgQueue ${klgQueue.value}")
                        }

                    }
                    Log.d(TAG, "onEvent: klgQueue final ${klgQueue.value}")

                    if (isKlgValid.value && isRutaValid.value) {
                        klgQueue.value.forEach { klg ->
                            insertKeluarga(klg)
                            klg.ruta.forEach { rt ->
                                insertRuta(rt)
                                insertKeluargaAndRuta(klg.kodeKlg, rt.kodeRuta)
                            }
                        }
                    } else if (isKlgValid.value && state.value.jmlKlg == 1) {
                        klgQueue.value.forEach { klg ->
                            insertKeluarga(klg)
                        }
                    }
                }
            }

            IsiRutaScreenEvent.edit -> {
                if (isListRuta == true) {
//                    var keluarga = Keluarga(
//                        SLS = state.value.SLS,
//                        noSegmen = state.value.noSegmen,
//                        noBgFisik = state.value.noBgFisik,
//                        noBgSensus = state.value.noBgSensus,
//                        noUrutKlg = state.value.listNoUrutKlg[0],
//                        namaKK = state.value.listNamaKK[0],
//                        alamat = state.value.listAlamat[0],
//                        isGenzOrtu = state.value.listIsGenzOrtu[0],
//                        noUrutKlgEgb = state.value.listNoUrutKlgEgb[0],
//                        penglMkn = state.value.listPenglMkn[0],
//                        idBS = idBS,
//                        kodeKlg = "K${idBS}${state.value.noSegmen}${UtilFunctions.padWithZeros(state.value.listNoUrutKlg[0], 3)}",
//                        nimPencacah = session?.nim ?: "",
//                        status = "update"
//                    )

                    val ruta = Ruta(
                        kodeRuta = ruta.value.kodeRuta,
                        noUrutRuta = state.value.listNoUrutRuta[0][0],
                        noUrutEgb = 0,
                        kkOrKrt = when (state.value.listKkOrKrt[0][0]) {
                            "Kepala Keluarga (KK) saja" -> "1"
                            "Kepala Rumah Tangga (KRT) saja" -> "2"
                            "KK Sekaligus KRT" -> "3"
                            else -> "0"
                        },
                        namaKrt = state.value.listNamaKrt[0][0],
                        jmlGenzAnak = state.value.listJmlGenzAnak[0][0],
                        jmlGenzDewasa = state.value.listJmlGenzDewasa[0][0],
                        katGenz = when {
                            state.value.listJmlGenzAnak[0][0] > 0 && state.value.listJmlGenzDewasa[0][0] == 0 -> 1
                            state.value.listJmlGenzAnak[0][0] == 0 && state.value.listJmlGenzDewasa[0][0] > 0 -> 2
                            state.value.listJmlGenzAnak[0][0] > 0 && state.value.listJmlGenzDewasa[0][0] > 0 -> 3
                            else -> 0
                        },
                        long = ruta.value.longitude,
                        lat = ruta.value.latitude,
                        catatan = state.value.listCatatan[0][0],
                        idBS = idBS,
                        nimPencacah = session?.nim ?: "",
                        noSegmen = ruta.value.noSegmen,
                        status = "update"
                    )

                    validateRuta(ruta, 0, 0)
                    if (isRutaValid.value) {
                        viewModelScope.launch (Dispatchers.IO) {
                            updateStatusListKeluarga(allKodeKlg)
                            updateRuta(ruta)
                        }
                    }
                } else {
                    var keluarga = Keluarga(
                        SLS = state.value.SLS,
                        noSegmen = state.value.noSegmen,
                        noBgFisik = state.value.noBgFisik,
                        noBgSensus = state.value.noBgSensus,
                        noUrutKlg = state.value.listNoUrutKlg[0],
                        namaKK = state.value.listNamaKK[0],
                        alamat = state.value.listAlamat[0],
                        isGenzOrtu = state.value.listIsGenzOrtu[0],
                        noUrutKlgEgb = state.value.listNoUrutKlgEgb[0],
                        penglMkn = state.value.listPenglMkn[0],
                        idBS = idBS,
                        kodeKlg = keluarga.value.kodeKlg,
                        nimPencacah = session?.nim ?: "",
                        status = "update"
                    )
                    validateKlg(keluarga, 0)
                    if (isKlgValid.value) {
                        updateKeluarga(keluarga)
                    }
                }
            }
        }
        Log.d(TAG, "onEvent: Finish!")
    }

    suspend fun validateKlg(klg: Keluarga, index: Int) {
        // viewModelScope.launch {

        getKeluarga(klg.kodeKlg)
        delay(500L)

        Log.d(TAG, "validateKlg: Start!")
        var tempState = state.value

        tempState = tempState.copy(
            SLSMsg = if (klg.SLS == "") {
                Message(null, "Error: Kolom SLS tidak boleh kosong!")
            } else {
                Message()
            },
            noSegmenMsg = if (klg.noSegmen == "") {
                Message(null, "Error: Kolom No Segmen tidak boleh kosong!")
            } else {
                if (!UtilFunctions.isValidNoSegmenFormat(state.value.noSegmen)) {
                    Message(null, "Error: No segmen tidak valid! (Format No segmen: Sxxx)")
                } else {
                    Message()
                }
            },
            noBgFisikMsg = if (klg.noBgFisik == "") {
                Message(null, "Error: Kolom No Bangungan Fisik tidak boleh kosong!")
            } else {
                Message()
            },
            noBgSensusMsg = if (klg.noBgSensus == "") {
                Message(null, "Error: Kolom No Bangungan Sensus tidak boleh kosong!")
            } else {
                if (state.value.noBgSensus < state.value.noBgFisik) {
                    Message(null, "Error: Bangunan sensus harus >= bangunan fisik!")
                } else {
                    Message()
                }
            }
        )


        Log.d(TAG, "validateKlg: Validate no urut klg!")
        Log.d(TAG, "validateKlg: ${UtilFunctions.convertStringToNumber(keluarga.value.noUrutKlg)} == ${UtilFunctions.convertStringToNumber(klg.noUrutKlg)}")

        val newListNoUrutKlgMsg = tempState.listNoUrutKlgMsg.toMutableList()
        if (klg.status == "insert") {
            newListNoUrutKlgMsg[index] = if (UtilFunctions.convertStringToNumber(keluarga.value.noUrutKlg) == UtilFunctions.convertStringToNumber(klg.noUrutKlg)) {
                Message(null, "Error: No urut keluarga telah ada sebelumnya!")
            } else {
                Message()
            }
        }

        val newListNamaKKMsg = tempState.listNamaKKMsg.toMutableList()
        newListNamaKKMsg[index] = if (klg.namaKK == "") {
            Message(null, "Error: Kolom Nama Kepala Keluarga tidak boleh kosong!")
        } else {
            Message()
        }

        val newListAlamatMsg = tempState.listAlamatMsg.toMutableList()
        newListAlamatMsg[index] = if (klg.alamat == "") {
            Message(null, "Error: Kolom Alamat tidak boleh kosong!")
        } else {
            Message()
        }

        if (tempState.jmlKlg == 1 && klg.isGenzOrtu == 0) {
            // Update state sekali dengan semua perubahan
            _state.emit(
                tempState.copy(
                    listNoUrutKlgMsg = newListNoUrutKlgMsg,
                    listNamaKKMsg = newListNamaKKMsg,
                    listAlamatMsg = newListAlamatMsg
                )
            )

            // Cek apakah ada error, jika tidak maka set isKlgValid menjadi true
            val isNoError = listOf(
                tempState.SLSMsg,
                tempState.noSegmenMsg,
                tempState.noBgFisikMsg,
                tempState.noBgSensusMsg,
                *newListNoUrutKlgMsg.toTypedArray(),
                *newListNamaKKMsg.toTypedArray(),
                *newListAlamatMsg.toTypedArray()
            ).all { it.error == null }

            _isKlgValid.emit(isNoError) // Emit nilai isNoError langsung

            Log.d(TAG, "validateKlg: isKlgValid=$isNoError")
            Log.d(TAG, "validateKlg: tempState=$tempState")


        } else {

            val newListNoUrutKlgEgbMsg = tempState.listNoUrutKlgEgbMsg.toMutableList()
            newListNoUrutKlgEgbMsg[index] = if (klg.noUrutKlgEgb == lastKeluargaEgb.value.noUrutKlgEgb) {
                Message(null, "Error: No urut keluarga eligible telah ada sebelumnya!")
            } else {
                Message()
            }

            // Update state sekali dengan semua perubahan
            _state.emit(
                tempState.copy(
                    listNoUrutKlgMsg = newListNoUrutKlgMsg,
                    listNamaKKMsg = newListNamaKKMsg,
                    listAlamatMsg = newListAlamatMsg,
                    listNoUrutKlgEgbMsg = newListNoUrutKlgEgbMsg
                )
            )

            // Cek apakah ada error, jika tidak maka set isKlgValid menjadi true
            val isNoError = listOf(
                tempState.SLSMsg,
                tempState.noSegmenMsg,
                tempState.noBgFisikMsg,
                tempState.noBgSensusMsg,
                *newListNoUrutKlgMsg.toTypedArray(),
                *newListNamaKKMsg.toTypedArray(),
                *newListAlamatMsg.toTypedArray(),
                *newListNoUrutKlgEgbMsg.toTypedArray()
            ).all { it.error == null }

            _isKlgValid.emit(isNoError) // Emit nilai isNoError langsung

            Log.d(TAG, "validateKlg: isKlgValid=$isNoError")
            Log.d(TAG, "validateKlg: tempState=$tempState")

        }
    }


//    suspend fun validateKlg(klg: Keluarga, index: Int) {
////        viewModelScope.launch {
//
//            getKeluarga(klg.kodeKlg)
//            delay(500L)
//
//            Log.d(TAG, "validateKlg: Start!")
//            var tempState = state.value
//
//            tempState = tempState.copy(
//                SLSMsg = if (klg.SLS == "") {
//                    Message(null, "Error: Kolom SLS tidak boleh kosong!")
//                } else {
//                    Message()
//                },
//                noSegmenMsg = if (klg.noSegmen == "") {
//                    Message(null, "Error: Kolom No Segmen tidak boleh kosong!")
//                } else {
//                    if (!UtilFunctions.isValidNoSegmenFormat(state.value.noSegmen)) {
//                        Message(null, "Error: No segmen tidak valid! (Format No segmen: Sxxx)")
//                    } else {
//                        Message()
//                    }
//                },
//                noBgFisikMsg = if (klg.noBgFisik == "") {
//                    Message(null, "Error: Kolom No Bangungan Fisik tidak boleh kosong!")
//                } else {
//                    Message()
//                },
//                noBgSensusMsg = if (klg.noBgSensus == "") {
//                    Message(null, "Error: Kolom No Bangungan Sensus tidak boleh kosong!")
//                } else {
//                    if (state.value.noBgSensus < state.value.noBgFisik) {
//                        Message(null, "Error: Bangunan sensus harus >= bangunan fisik!")
//                    } else {
//                        Message()
//                    }
//                }
//            )
//
//
//            Log.d(TAG, "validateKlg: Validate no urut klg!")
//            Log.d(TAG, "validateKlg: ${UtilFunctions.convertStringToNumber(keluarga.value.noUrutKlg)} == ${UtilFunctions.convertStringToNumber(klg.noUrutKlg)}")
//
//            val newListNoUrutKlgMsg = tempState.listNoUrutKlgMsg.toMutableList()
//            if (klg.status == "insert") {
//                newListNoUrutKlgMsg[index] = if (UtilFunctions.convertStringToNumber(keluarga.value.noUrutKlg) == UtilFunctions.convertStringToNumber(klg.noUrutKlg)) {
//                    Message(null, "Error: No urut keluarga telah ada sebelumnya!")
//                } else {
//                    Message()
//                }
//            }
//
//            val newListNamaKKMsg = tempState.listNamaKKMsg.toMutableList()
//            newListNamaKKMsg[index] = if (klg.namaKK == "") {
//                Message(null, "Error: Kolom Nama Kepala Keluarga tidak boleh kosong!")
//            } else {
//                Message()
//            }
//
//            val newListAlamatMsg = tempState.listAlamatMsg.toMutableList()
//            newListAlamatMsg[index] = if (klg.alamat == "") {
//                Message(null, "Error: Kolom Alamat tidak boleh kosong!")
//            } else {
//                Message()
//            }
//
//            val newListNoUrutKlgEgbMsg = tempState.listNoUrutKlgEgbMsg.toMutableList()
//            newListNoUrutKlgEgbMsg[index] = if (klg.noUrutKlgEgb == lastKeluargaEgb.value.noUrutKlgEgb) {
//                Message(null, "Error: No urut keluarga eligible telah ada sebelumnya!")
//            } else {
//                Message()
//            }
//
//            // Update state sekali dengan semua perubahan
//            _state.emit(tempState.copy(
//                listNoUrutKlgMsg = newListNoUrutKlgMsg,
//                listNamaKKMsg = newListNamaKKMsg,
//                listAlamatMsg = newListAlamatMsg,
//                listNoUrutKlgEgbMsg = newListNoUrutKlgEgbMsg
//            ))
//
//            // Cek apakah ada error, jika tidak maka set isKlgValid menjadi true
//            val isNoError = listOf(tempState.SLSMsg, tempState.noSegmenMsg, tempState.noBgFisikMsg, tempState.noBgSensusMsg, *newListNoUrutKlgMsg.toTypedArray(), *newListNamaKKMsg.toTypedArray(), *newListAlamatMsg.toTypedArray(), *newListNoUrutKlgEgbMsg.toTypedArray()).all { it.error == null }
//
//            if (isNoError) {
//                _isKlgValid.emit(true)
//            } else {
//                _isKlgValid.emit(false)
//            }
//        Log.d(TAG, "validateKlg: isKlgValid=${isKlgValid.value}")
////        }
//    }

    suspend fun validateRuta(
        rt: Ruta,
        indexKlg: Int,
        indexRuta: Int
    ) {
        getRuta(rt.kodeRuta)
        delay(500L)

        var tempState = state.value

        val newListNoUrutRutaMsg = tempState.listNoUrutRutaMsg.map { it.toMutableList() }.toMutableList()
        if (rt.status == "insert") {
            newListNoUrutRutaMsg[indexKlg][indexRuta] = if (UtilFunctions.convertStringToNumber(ruta.value.noUrutRuta) == UtilFunctions.convertStringToNumber(rt.noUrutRuta)) {
                Message(null, "Error: No urut ruta telah ada sebelumnya!")
            } else {
                Message()
            }
        }

        val newListKkOrKrtMsg = tempState.listKkOrKrtMsg.map { it.toMutableList() }.toMutableList()
        newListKkOrKrtMsg[indexKlg][indexRuta] = if (rt.kkOrKrt == "0") {
            Message(null, "Error: Identifikasi KK/KRT tidak boleh kosong!")
        } else {
            Message()
        }

        val newListNamaKrtMsg = tempState.listNamaKrtMsg.map { it.toMutableList() }.toMutableList()
        newListNamaKrtMsg[indexKlg][indexRuta] = if (rt.namaKrt.isEmpty()) {
            Message(null, "Error: Kolom nama kepala rumah tangga tidak boleh kosong!")
        } else {
            Message()
        }

        // Perbarui tempState dengan list baru
        tempState = tempState.copy(
            listNoUrutRutaMsg = newListNoUrutRutaMsg,
            listKkOrKrtMsg = newListKkOrKrtMsg,
            listNamaKrtMsg = newListNamaKrtMsg
        )

        // Emit state yang sudah diperbarui
        _state.emit(tempState)

        // Cek apakah ada error di pesan yang baru diperbarui
        val isNoError = tempState.listNoUrutRutaMsg.flatten().all { it.error == null } &&
                tempState.listKkOrKrtMsg.flatten().all { it.error == null } &&
                tempState.listNamaKrtMsg.flatten().all { it.error == null }

        // Set _isRutaValid berdasarkan hasil pengecekan error
        _isRutaValid.emit(isNoError)
        Log.d(TAG, "validateKlg: isRutaValid=${isRutaValid.value}")
    }

//    private fun <T> updateList(list: List<List<T>>, index: Int, diff: Int, defaultValue: T): List<List<T>> {
//        return list.mapIndexed { i, listItem ->
//            if (i == index) {
//                listItem + List(diff.coerceAtLeast(0)) { defaultValue }
//            } else {
//                listItem
//            }
//        }
//    }

    private fun <T> updateListAtIndex(list: List<List<T>>, index: Int, newSize: Int): List<List<T>> {
        return list.mapIndexed { i, listItem ->
            if (i == index) listItem.take(newSize).toMutableList() else listItem.toMutableList()
        }.toMutableList()
    }

    private suspend inline fun <reified T> updateListAtIndex2(
        list: List<List<T>>,
        index: Int,
        index2: Int,
        value: T,
        updateState: (IsiRutaScreenState) -> IsiRutaScreenState
    ) {
        val newList = list.toMutableList()
        newList[index] = newList[index].toMutableList().apply {
            set(index2, value)
        }
        _state.emit(updateState(state.value.copy()))
    }

//    fun increment(input: String?): String {
//        val numericPart = input?.filter { it.isDigit() }
//        val number = numericPart?.toInt()
//        val formattedNumber = String.format("%0${numericPart?.length}d", number?.plus(1) ?: 0)
//        return input?.replaceFirst(numericPart.toString(), formattedNumber) ?: ""
//    }

    fun increment(rawInput: String?): String {

        val input = UtilFunctions.checkStringAngka(rawInput.toString())

        // Cek jika input kosong atau null, langsung kembalikan "0"
        if (input.isNullOrEmpty()) return "0"

        val numericPart = input.filter { it.isDigit() }
        val number = numericPart.toIntOrNull()
        val formattedNumber = String.format("%0${numericPart.length}d", number?.plus(1) ?: 0)

        return input.replaceFirst(numericPart, formattedNumber)
    }


//    fun decrement(input: String?): String {
//        val numericPart = input?.filter { it.isDigit() }
//        val number = numericPart?.toInt()
//        if (number != null) {
//            if (number < 1) {
//                return input.toString()
//            }
//        }
//        val formattedNumber = String.format("%0${numericPart?.length}d", number?.minus(1) ?: 0)
//        return input?.replaceFirst(numericPart.toString(), formattedNumber) ?: ""
//    }

    fun decrement(rawInput: String?): String {

        val input = UtilFunctions.checkStringAngka(rawInput.toString())

        // Cek jika input kosong atau null, langsung kembalikan "0"
        if (input.isNullOrEmpty()) return "0"

        val numericPart = input.filter { it.isDigit() }
        val number = numericPart.toIntOrNull()

        // Jika number tidak null dan lebih dari 0, lakukan decrement
        // Jika number sama dengan 0, kembalikan "0"
        if (number != null) {
            if (number < 1) {
                return "0"
            }
        } else {
            // Jika tidak ada bagian numerik, kembalikan input asli
            return input
        }

        val formattedNumber = String.format("%0${numericPart.length}d", number.minus(1))
        return input.replaceFirst(numericPart, formattedNumber)
    }


//    fun incrementNoSegmen(input: String?): String {
//        val numericPart = input?.filter { it.isDigit() }
//        val number = numericPart?.toInt()
//        val formattedNumber = String.format("%0${numericPart?.length}d", number?.plus(10) ?: 0)
//        return input?.replaceFirst(numericPart.toString(), formattedNumber) ?: ""
//    }

    fun incrementNoSegmen(input: String?): String {
        // Cek jika input kosong atau null, langsung kembalikan "S000"
        if (input.isNullOrEmpty()) return "S000"

        val numericPart = input.filter { it.isDigit() }
        val number = numericPart.toIntOrNull()
        // Menambahkan 10 ke bagian numerik. Jika input tidak mengandung angka, default ke 0.
        val formattedNumber = String.format("%0${numericPart.length.takeIf { it > 0 } ?: 3}d", number?.plus(10) ?: 0)

        // Jika input tidak mengandung bagian numerik, return "S000",
        // karena tidak ada angka yang bisa diincrement dan format default untuk kasus kosong adalah "S000"
        if (numericPart.isEmpty()) return "S000"

        // Mengganti bagian numerik dari input dengan formattedNumber. Jika tidak ada numericPart, kembalikan format default.
        return input.replaceFirst(numericPart, formattedNumber)
    }


    private fun incrementStringNoSegmen(input: String): String {
        val numberPart = input.filter { it.isDigit() }
        val incrementedNumber = numberPart.toInt() + 10
        return "S" + String.format("%03d", incrementedNumber)
    }

//    fun decrementNoSegmen(input: String?): String {
//        val numericPart = input?.filter { it.isDigit() }
//        val number = numericPart?.toInt()
//        if (number != null) {
//            if (number < 10) {
//                return input.toString()
//            }
//        }
//        val formattedNumber = String.format("%0${numericPart?.length}d", number?.minus(10) ?: 0)
//        return input?.replaceFirst(numericPart.toString(), formattedNumber) ?: ""
//    }

    fun decrementNoSegmen(input: String?): String {
        // Cek jika input kosong atau null, langsung kembalikan "S000"
        if (input.isNullOrEmpty()) return "S000"

        val numericPart = input.filter { it.isDigit() }
        val number = numericPart.toIntOrNull()

        // Cek jika number kurang dari 10 atau null, mengembalikan "S000" sebagai default
        if (number == null || number < 10) {
            return "S000"
        }

        val formattedNumber = String.format("%0${numericPart.length}d", number.minus(10))
        return input.replaceFirst(numericPart, formattedNumber)
    }


//    fun incrementHuruf(input: String): String {
//        val lastChar = input.last()
//        return if (lastChar.isDigit()) {
//            input + "A"
//        } else {
//            val nextChar = if (lastChar == 'Z') 'A' else lastChar + 1
//            if (nextChar == 'A') {
//                input.dropLast(1) + nextChar + "A"
//            } else {
//                input.dropLast(1) + nextChar
//            }
//        }
//    }

    fun incrementHuruf(input: String): String {
        // Cek jika input kosong, langsung kembalikan "1"
        if (input.isEmpty()) return "1"

        val lastChar = input.last()
        return if (lastChar.isDigit()) {
            input + "A"
        } else {
            val nextChar = if (lastChar == 'Z') 'A' else lastChar.inc()
            if (nextChar == 'A') {
                input.dropLast(1) + nextChar + "A"
            } else {
                input.dropLast(1) + nextChar
            }
        }
    }


//    fun decrementHuruf(input: String): String {
//        val lastChar = input.last()
//        return if (lastChar == 'A' && input.length > 1) {
//            input.dropLast(1)
//        } else if (lastChar > 'A') {
//            input.dropLast(1) + (lastChar - 1)
//        } else {
//            input
//        }
//    }

    fun decrementHuruf(input: String): String {
        // Cek jika input kosong, langsung kembalikan "1"
        if (input.isEmpty()) return "1"

        val lastChar = input.lastOrNull() ?: return "1" // Jika input kosong, kembalikan "1"

        return when {
            lastChar == 'A' && input.length > 1 -> input.dropLast(1)
            lastChar > 'A' -> input.dropLast(1) + (lastChar - 1)
            else -> input // Jika lastChar adalah 'A' dan input.length == 1, atau karakter tidak valid untuk decrement
        }
    }


    fun generateRandomDigitString(length: Int): String {
        val stringBuilder = StringBuilder(length)
        for (i in 1..length) {
            val number = Random.nextInt(0, 10)
            stringBuilder.append(number)
        }
        return stringBuilder.toString()
    }
}