package com.polstat.pkl.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.relation.WilayahWithAll
import com.polstat.pkl.mapper.toWilayah
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Lokasi
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.ui.event.IsiRutaScreenEvent
import com.polstat.pkl.ui.state.IsiRutaScreenState
import com.polstat.pkl.utils.Result
import com.polstat.pkl.utils.UtilFunctions
import com.polstat.pkl.utils.location.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IsiRutaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val wilayahRepository: WilayahRepository,
    private val keluargaRepository: KeluargaRepository,
    private val getLocationUseCase: GetLocationUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _state = MutableStateFlow(IsiRutaScreenState())

    val state = _state.asStateFlow()

    val noBS = savedStateHandle.get<String>("noBS")

    private val _lastKeluarga = MutableStateFlow(KeluargaEntity())

    val lastKeluarga = _lastKeluarga.asStateFlow()

    private val _lastKeluargaEgb = MutableStateFlow(KeluargaEntity())

    val lastKeluargaEgb = _lastKeluargaEgb.asStateFlow()

    private val _lastRuta = MutableStateFlow(RutaEntity())

    val lastRuta = _lastRuta.asStateFlow()

    private val _lokasi = MutableStateFlow(Lokasi())

    val lokasi = _lokasi.asStateFlow()

    private val _wilayahWithAll = MutableStateFlow(WilayahWithAll())

    val wilayahWithAll = _wilayahWithAll.asStateFlow()

    init {
        getLastKeluarga(noBS!!)
        getLastKeluargaEgb(noBS)
        getLastRuta(noBS)

//        viewModelScope.launch {
//            _state.emit(
//                state.value.copy(
//                    noUrutKlg = lastKeluarga.value.noUrutKlg!! + 1,
//                    noUrutKlgEgb = lastKeluarga.value.noUrutKlgEgb!! + 1
//                )
//            )
//        }

    }



    var lat by mutableStateOf(0.0)

    var long by mutableStateOf(0.0)

    companion object {
        private const val TAG = "CAPI63_ISI_RUTA_VM"
    }

    fun insertRuta(
        ruta: Ruta
    ){
        viewModelScope.launch {
            localRutaRepository.insertRuta(ruta).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    fun insertKeluarga(
        keluarga: Keluarga
    ) {
        viewModelScope.launch {
            keluargaRepository.insertKeluarga(keluarga).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    fun insertKeluargaAndRuta(
        kodeKlg: String,
        kodeRuta: String
    ) {
        viewModelScope.launch {
            localRutaRepository.insertKeluargaAndRuta(kodeKlg, kodeRuta).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
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

    fun updateRekapitulasiWilayah(
        noBS: String,
        nim: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithAll(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _wilayahWithAll.value = response
                            Log.d(TAG, "getWilayahWithAll success: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getWilayahWithAll: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            Log.e(TAG, "getWilayahWithAll: Error in getWilayahWithAll ($error)")
                        }
                    }
                }
            }
        }

        val wilayah = wilayahWithAll.value.wilayahWithKeluarga!!.wilayah!!.toWilayah(emptyList())
        val updatedWilayah = wilayah.copy(
            jmlKlg = wilayahWithAll.value.listKeluargaWithRuta!!.filter { it.keluarga.status != "delete" }.size,
            jmlKlgEgb = wilayahWithAll.value.listKeluargaWithRuta!!.filter { it.keluarga.status != "delete" && it.keluarga.noUrutKlgEgb != 0 }.size,
            jmlRuta = wilayahWithAll.value.listKeluargaWithRuta!!.flatMap { it.listRuta.filter { it.status != "delete" } }.size,
            jmlRutaEgb = wilayahWithAll.value.listKeluargaWithRuta!!.flatMap { it.listRuta.filter { it.status != "delete" && it.noUrutEgb != 0 }}.size
        )

        viewModelScope.launch {
            wilayahRepository.updateWilayah(updatedWilayah, nim).collectLatest { message ->
                Log.d(TAG, message)
            }
        }

    }

    fun getLastKeluarga(
        noBS: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithAll(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.wilayahWithKeluarga!!.listKeluarga?.let { listKeluarga ->
                            _lastKeluarga.value = listKeluarga.filter { it.status != "delete" }.sortedBy { it.noUrutKlg }.lastOrNull()!!
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getLastKeluarga: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            Log.d(TAG, "Error getLastKeluarga: $error")
                        }
                    }
                }
            }
        }
    }

    fun getLastKeluargaEgb(
        noBS: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithAll(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.wilayahWithKeluarga!!.listKeluarga?.let { listKeluarga ->
                            _lastKeluargaEgb.value = listKeluarga.filter { it.status != "delete" && it.noUrutKlgEgb != 0 }.sortedBy { it.noUrutKlgEgb }.lastOrNull()!!
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getLastKeluargaEgb: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            Log.d(TAG, "Error getLastKeluargaEgb: $error")
                        }
                    }
                }
            }
        }
    }

    fun getLastRuta(
        noBS: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithRuta(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.listRuta?.let { listRuta ->
                            _lastRuta.value = listRuta.filter { it.status != "delete" }.sortedBy { it.noUrutRuta }.lastOrNull()!!
                        }
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            Log.d(TAG, "Error getLastRuta: $error")
                        }
                    }

                    is Result.Loading -> {
                        Log.d(TAG, "getLastRuta: Loading...")
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun onEvent(
        event: IsiRutaScreenEvent,
        index: Int = 0
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
            is IsiRutaScreenEvent.NoUrutKlgChanged -> {
                _state.emit(state.value.copy(noUrutKlg = event.noUrutKlg))
            }
            is IsiRutaScreenEvent.NamaKKChanged -> {
                _state.emit(state.value.copy(namaKK = event.namaKK))
            }
            is IsiRutaScreenEvent.AlamatChanged -> {
                _state.emit(state.value.copy(alamat = event.alamat))
            }
            is IsiRutaScreenEvent.IsGenzOrtuChanged -> {
                _state.emit(state.value.copy(isGenzOrtu = event.isGenzOrtu))
            }
            is IsiRutaScreenEvent.NoUrutKlgEgbChanged -> {
                _state.emit(state.value.copy(noUrutKlgEgb = event.noUrutKlgEgb))
            }
            is IsiRutaScreenEvent.PenglMknChanged -> {
                if (event.penglMkn > 0) {
                    _state.emit(
                        state.value.copy(
                            listNoUrutRuta = List(event.penglMkn) { 0 },
                            listKatGenz = List(event.penglMkn) { 0 },
                            listGenzOrtu = List(event.penglMkn) { 0 },
                            listKkOrKrt = List(event.penglMkn) { "" },
                            listNamaKrt = List(event.penglMkn) { "" }
                        )
                    )
                }
                _state.emit(state.value.copy(penglMkn = event.penglMkn))
            }
            is IsiRutaScreenEvent.NoUrutRutaChanged -> {
                val newListNoUrutRuta = state.value.listNoUrutRuta?.toMutableList()
                newListNoUrutRuta!![index] = event.noUrutRuta
                _state.emit(state.value.copy(listNoUrutRuta = newListNoUrutRuta))
            }
            is IsiRutaScreenEvent.KKOrKRTChanged -> {
                val newListKkOrKrt = state.value.listKkOrKrt?.toMutableList()
                newListKkOrKrt!![index] = event.kkOrKRT
                _state.emit(state.value.copy(listKkOrKrt = newListKkOrKrt))
            }
            is IsiRutaScreenEvent.NamaKRTChanged -> {
                val newListNamaKrt = state.value.listNamaKrt?.toMutableList()
                newListNamaKrt!![index] = event.namaKRT
                _state.emit(state.value.copy(listNamaKrt = newListNamaKrt))
            }
            is IsiRutaScreenEvent.GenzOrtuChanged -> {
                val newListGenzOrtu = state.value.listGenzOrtu?.toMutableList()
                newListGenzOrtu!![index] = event.genzOrtu
                _state.emit(state.value.copy(listGenzOrtu = newListGenzOrtu))
            }
            is IsiRutaScreenEvent.KatGenzChanged -> {
                val newListKatGenz = state.value.listKatGenz?.toMutableList()
                newListKatGenz!![index] = event.katGenz
                _state.emit(state.value.copy(listKatGenz = newListKatGenz))
            }
            is IsiRutaScreenEvent.submit -> {
                val keluarga = Keluarga(
                    SLS = state.value.SLS,
                    noSegmen = state.value.noSegmen,
                    noBgFisik = state.value.noBgFisik,
                    noBgSensus = state.value.noBgSensus,
                    noUrutKlg = state.value.noUrutKlg,
                    namaKK = state.value.namaKK,
                    alamat = state.value.alamat,
                    isGenzOrtu = state.value.isGenzOrtu,
                    noUrutKlgEgb = state.value.noUrutKlgEgb,
                    penglMkn = state.value.penglMkn,
                    noBS = noBS,
                    kodeKlg = "K"+ noBS + UtilFunctions.convertTo3DigitsString(state.value.noUrutKlg!!),
                    status = "insert"
                )

                insertKeluarga(keluarga)

                state.value.penglMkn?.let {
                    repeat(it) {
                        val genzOrtuRuta = state.value.listGenzOrtu!![it]

                        val ruta = Ruta(
                            kodeRuta = "R" + noBS + UtilFunctions.convertTo3DigitsString(state.value.listNoUrutRuta!![it]),
                            noUrutRuta = state.value.listNoUrutRuta!![it],
                            noUrutEgb = 0,
                            kkOrKrt = state.value.listKkOrKrt!![it],
                            namaKrt = state.value.listNamaKrt!![it],
                            isGenzOrtu = genzOrtuRuta,
                            katGenz = if (genzOrtuRuta >= 1 && genzOrtuRuta <= 2) 1 else if (genzOrtuRuta >= 3 && genzOrtuRuta <= 4) 2 else if (genzOrtuRuta > 4) 3 else 0,
                            long = lokasi.value.longitude,
                            lat = lokasi.value.latitude,
                            catatan = "",
                            noBS = noBS,
                            status = "insert"
                        )



                        insertRuta(ruta)

                        insertKeluargaAndRuta(keluarga.kodeKlg, ruta.kodeRuta)
                    }
                }

                updateRekapitulasiWilayah(
                    noBS = noBS!!,
                    nim = session!!.nim!!
                )
            }
        }
    }

    fun convertIsGenzOrtu(isGenzOrtu: String): String{
        return when(isGenzOrtu){
            "Ya" -> "1"
            "Tidak" -> "0"
            else -> ""
        }
    }
}