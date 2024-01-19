package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.ui.event.IsiRutaScreenEvent
import com.polstat.pkl.ui.state.IsiRutaScreenState
import com.polstat.pkl.utils.Result
import com.polstat.pkl.utils.UtilFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IsiRutaViewModel @Inject constructor(
    private val localRutaRepository: LocalRutaRepository,
    private val wilayahRepository: WilayahRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val _state = MutableStateFlow(IsiRutaScreenState())
    val state = _state.asStateFlow()

    val noBS = savedStateHandle.get<String>("noBS")!!

    private val _lastRuta = MutableStateFlow(RutaEntity())

    val lastRuta = _lastRuta.asStateFlow()

    private val _ruta = MutableStateFlow(RutaEntity())

    val ruta = _ruta.asStateFlow()

    init {

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

    fun getLastRuta() {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithRuta(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.listRuta?.let { listRuta ->
                            _lastRuta.value = listRuta.maxByOrNull { it.noUrutRuta!! }!!
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

    fun getRuta(noRuta: String) {
        viewModelScope.launch {
            localRutaRepository.getRuta(noRuta).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        _ruta.value = result.data!!
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

    suspend fun onEvent(
        event: IsiRutaScreenEvent,
        index: Int = 0
    ) {
        val newListNoUrutRuta = state.value.listNoUrutRuta?.toMutableList()
        val newListKkOrKrt = state.value.listKkOrKrt?.toMutableList()
        val newListNamaKrt = state.value.listNamaKrt?.toMutableList()
        val newListGenzOrtu = state.value.listGenzOrtu?.toMutableList()
        val newListKatGenz = state.value.listKatGenz?.toMutableList()
        val newListKodeRuta = state.value.listKodeRuta?.toMutableList()

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
                _state.emit(state.value.copy(penglMkn = event.penglMkn))
            }
            is IsiRutaScreenEvent.NoUrutRutaChanged -> {
                _state.emit(state.value.copy(noUrutRuta = event.noUrutRuta))
                newListNoUrutRuta!!.add(0)
                newListNoUrutRuta[index] = event.noUrutRuta
                _state.emit(state.value.copy(listNoUrutRuta = newListNoUrutRuta))
            }
            is IsiRutaScreenEvent.KKOrKRTChanged -> {
                _state.emit(state.value.copy(kkOrKrt = event.kkOrKRT))
                newListKkOrKrt!!.add("")
                newListKkOrKrt[index] = event.kkOrKRT
                _state.emit(state.value.copy(listKkOrKrt = newListKkOrKrt))
            }
            is IsiRutaScreenEvent.NamaKRTChanged -> {
                _state.emit(state.value.copy(namaKrt = event.namaKRT))
                newListNamaKrt!!.add("")
                newListNamaKrt[index] = event.namaKRT
                _state.emit(state.value.copy(listNamaKrt = newListNamaKrt))
            }
            is IsiRutaScreenEvent.GenzOrtuChanged -> {
                _state.emit(state.value.copy(genzOrtu = event.genzOrtu))
                newListGenzOrtu!!.add(0)
                newListGenzOrtu[index] = event.genzOrtu
                _state.emit(state.value.copy(listGenzOrtu = newListGenzOrtu))
            }
            is IsiRutaScreenEvent.KatGenzChanged -> {
                _state.emit(state.value.copy(katGenz = event.katGenz))
                newListKatGenz!!.add(0)
                newListKatGenz[index] = event.katGenz
                _state.emit(state.value.copy(listKatGenz = newListKatGenz))
            }
            is IsiRutaScreenEvent.submit -> {
                val keluarga = Keluarga(
                    SLS = state.value.SLS,
                    noSegmen = state.value.noSegmen,
                    noBgFisik = state.value.noBgFisik?.toInt(),
                    noBgSensus = state.value.noBgSensus?.toInt(),
                    noUrutKlg = state.value.noUrutKlg,
                    namaKK = state.value.namaKK,
                    alamat = state.value.alamat,
                    isGenzOrtu = state.value.isGenzOrtu,
                    noUrutKlgEgb = state.value.noUrutKlgEgb,
                    penglMkn = state.value.penglMkn,
                    noBS = noBS,
                    kodeKlg = noBS + state.value.noUrutKlg,
                )
                state.value.penglMkn?.let {
                    repeat(it) {
                        val ruta = Ruta(
                            kodeRuta = "R" + noBS + UtilFunctions.convertTo3DigitsString(state.value.noUrutRuta!!),
                            noUrutRuta = state.value.listNoUrutRuta!![it - 1],
                            noUrutEgb = 0,
                            kkOrKrt = state.value.listKkOrKrt!![it - 1],
                            namaKrt = state.value.listNamaKrt!![it - 1],
                            isGenzOrtu = state.value.listGenzOrtu!![it - 1],
                            katGenz = state.value.listKatGenz!![it - 1],
                            long = long,
                            lat = lat,
                            catatan = "",
                            noBS = noBS,
                            status = "0"
                        )

                        val keluargaWithRuta = keluarga.copy(ruta = listOf(ruta))
                        Log.d(TAG, "keluarga : $keluargaWithRuta")
                        insertRuta(ruta)
                    }
                }
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