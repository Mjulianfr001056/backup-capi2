package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Lokasi
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.ui.event.IsiRutaScreenEvent
import com.polstat.pkl.ui.state.IsiRutaScreenState
import com.polstat.pkl.utils.Result
import com.polstat.pkl.utils.UtilFunctions
import com.polstat.pkl.utils.location.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val _lastKeluarga = MutableStateFlow(KeluargaEntity())

    val lastKeluarga = _lastKeluarga.asStateFlow()

    private val _lastKeluargaEgb = MutableStateFlow(KeluargaEntity())

    val lastKeluargaEgb = _lastKeluargaEgb.asStateFlow()

    private val _lastRuta = MutableStateFlow(RutaEntity())

    val lastRuta = _lastRuta.asStateFlow()

    private val _ruta = MutableStateFlow(RutaEntity())

    val ruta = _ruta.asStateFlow()

    private val _lokasi = MutableStateFlow(Lokasi())

    private val lokasi = _lokasi.asStateFlow()

    init {
        viewModelScope.launch {
            val getLastKeluargaTask = async { getLastKeluarga() }
            val getLastKeluargaEgbTask = async { getLastKeluargaEgb() }
            val getLastRutaTask = async { getLastRuta() }

            _lastKeluarga.value = getLastKeluargaTask.await()
            _lastKeluargaEgb.value = getLastKeluargaEgbTask.await()
            _lastRuta.value = getLastRutaTask.await()

            setInitialKlgValue()
        }
    }

    private fun setInitialKlgValue() {
        _state.value = state.value.copy(
            SLS = lastKeluarga.value.banjar,
            noSegmen = incrementStringNoSegmen(lastKeluarga.value.noSegmen),
            noBgFisik = UtilFunctions.convertStringToNumber(lastKeluarga.value.noBgFisik).plus(1).toString(),
            noBgSensus = UtilFunctions.convertStringToNumber(lastKeluarga.value.noBgSensus).plus(1).toString()
        )
        Log.d(TAG, "setInitialKlgValue: ${state.value}")
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

    private suspend fun getLastKeluarga(): KeluargaEntity {
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

        keluargaRepository.getLastKeluarga().collectLatest { result ->
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

    private suspend fun getLastKeluargaEgb(): KeluargaEntity {
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

        keluargaRepository.getLastKeluargaEgb().collectLatest { result ->
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

    private suspend fun getLastRuta(): RutaEntity {
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

        localRutaRepository.getLastRuta().collectLatest { result ->
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
            }
            is IsiRutaScreenEvent.NoBgFisikChanged -> {
                _state.emit(state.value.copy(noBgFisik = event.noBgFisik))
            }
            is IsiRutaScreenEvent.NoBgSensusChanged -> {
                _state.emit(state.value.copy(noBgSensus = event.noBgSensus))
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
                            listLat = state.value.listLat + List(diff) { emptyList() }
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
                            listLat = state.value.listLat.take(newSize)
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
                            ((newListNoUrutRuta[index - 1].map { UtilFunctions.convertStringToNumber(it) }.max().plus(1))..(newListNoUrutRuta[index - 1].map { UtilFunctions.convertStringToNumber(it) }.max().plus(newSize))).toList().map { it.toString() }
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

                        state.value.copy(
                            listNoUrutRuta = newListNoUrutRuta,
                            listKkOrKrt = newListKkOrKrt,
                            listNamaKrt = newListNamaKrt,
                            listJmlGenzAnak = newListJmlGenzAnak,
                            listJmlGenzDewasa = newListJmlGenzDewasa,
                            listKatGenz = newListKatGenz,
                            listLong = newListLong,
                            listLat = newListLat,
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

                        state.value.copy(
                            listNoUrutRuta = newListNoUrutRuta,
                            listKkOrKrt = newListKkOrKrt,
                            listNamaKrt = newListNamaKrt,
                            listJmlGenzAnak = newListJmlGenzAnak,
                            listJmlGenzDewasa = newListJmlGenzDewasa,
                            listKatGenz = newListKatGenz,
                            listLong = newListLong,
                            listLat = newListLat,
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

            is IsiRutaScreenEvent.submit -> {

                if (state.value.jmlKlg == 0) {
                    //Masih proses
                } else {
                    repeat(state.value.jmlKlg) { indexKlg ->
                        val keluarga = Keluarga(
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
                            kodeKlg = "K${idBS.takeLast(4)}${UtilFunctions.padWithZeros(state.value.listNoUrutKlg[indexKlg], 3)}",
                            nimPencacah = session?.nim ?: "",
                            status = "insert"
                        )

                        insertKeluarga(keluarga)

                        repeat(state.value.listPenglMkn[indexKlg]) { indexRuta ->
                            val ruta = Ruta(
                                kodeRuta = "R${idBS.takeLast(4)}${UtilFunctions.padWithZeros(state.value.listNoUrutRuta[indexKlg][indexRuta], 3)}",
                                noUrutRuta = state.value.listNoUrutRuta[indexKlg][indexRuta],
                                noUrutEgb = 0,
                                kkOrKrt = when (state.value.listKkOrKrt[indexKlg][indexRuta]) {
                                    "Kepala Keluarga (KK) saja" -> "1"
                                    "Kepala Rumah Tangga (KRT) saja" -> "2"
                                    else -> "3"
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
                                catatan = "",
                                idBS = idBS,
                                nimPencacah = session?.nim ?: "",
                                status = "insert"
                            )

                            insertRuta(ruta)

                            insertKeluargaAndRuta(keluarga.kodeKlg, ruta.kodeRuta)
                        }
                    }
                }
            }
        }
    }

    private fun <T> updateList(list: List<List<T>>, index: Int, diff: Int, defaultValue: T): List<List<T>> {
        return list.mapIndexed { i, listItem ->
            if (i == index) {
                listItem + List(diff.coerceAtLeast(0)) { defaultValue }
            } else {
                listItem
            }
        }
    }

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

    fun increment(input: String?): String {
        val numericPart = input?.filter { it.isDigit() }
        val number = numericPart?.toInt()
        val formattedNumber = String.format("%0${numericPart?.length}d", number?.plus(1) ?: 0)
        return input?.replaceFirst(numericPart.toString(), formattedNumber) ?: ""
    }

    fun decrement(input: String?): String {
        val numericPart = input?.filter { it.isDigit() }
        val number = numericPart?.toInt()
        if (number != null) {
            if (number < 1) {
                return input.toString()
            }
        }
        val formattedNumber = String.format("%0${numericPart?.length}d", number?.minus(1) ?: 0)
        return input?.replaceFirst(numericPart.toString(), formattedNumber) ?: ""
    }

    fun incrementNoSegmen(input: String?): String {
        val numericPart = input?.filter { it.isDigit() }
        val number = numericPart?.toInt()
        val formattedNumber = String.format("%0${numericPart?.length}d", number?.plus(10) ?: 0)
        return input?.replaceFirst(numericPart.toString(), formattedNumber) ?: ""
    }

    private fun incrementStringNoSegmen(input: String): String {
        val numberPart = input.filter { it.isDigit() }
        val incrementedNumber = numberPart.toInt() + 10
        return "S" + String.format("%03d", incrementedNumber)
    }

    fun decrementNoSegmen(input: String?): String {
        val numericPart = input?.filter { it.isDigit() }
        val number = numericPart?.toInt()
        if (number != null) {
            if (number < 10) {
                return input.toString()
            }
        }
        val formattedNumber = String.format("%0${numericPart?.length}d", number?.minus(10) ?: 0)
        return input?.replaceFirst(numericPart.toString(), formattedNumber) ?: ""
    }

    fun incrementHuruf(input: String): String {
        val lastChar = input.last()
        return if (lastChar.isDigit()) {
            input + "A"
        } else {
            val nextChar = if (lastChar == 'Z') 'A' else lastChar + 1
            if (nextChar == 'A') {
                input.dropLast(1) + nextChar + "A"
            } else {
                input.dropLast(1) + nextChar
            }
        }
    }

    fun decrementHuruf(input: String): String {
        val lastChar = input.last()
        return if (lastChar == 'A' && input.length > 1) {
            input.dropLast(1)
        } else if (lastChar > 'A') {
            input.dropLast(1) + (lastChar - 1)
        } else {
            input
        }
    }

}