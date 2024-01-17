package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.ui.event.IsiRutaScreenEvent
import com.polstat.pkl.ui.state.IsiRutaScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IsiRutaViewModel @Inject constructor(
    private val localRutaRepository: LocalRutaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    var state by mutableStateOf(IsiRutaScreenState())

    val noBS = savedStateHandle.get<String>("noBS")

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

    fun onEvent(event: IsiRutaScreenEvent){
        when(event) {
            is IsiRutaScreenEvent.SLSChanged -> {
                state = state.copy(SLS = event.sls)
            }

            is IsiRutaScreenEvent.NoSegmenChanged -> {
                state = state.copy(noSegmen = event.noSegmen)
            }

            is IsiRutaScreenEvent.NoBgFisikChanged -> {
                state = state.copy(noBgFisik = event.noBgFisik)
            }

            is IsiRutaScreenEvent.NoBgSensusChanged -> {
                state = state.copy(noBgSensus = event.noBgSensus)
            }

            is IsiRutaScreenEvent.NoUrutKlgChanged -> {
                state = state.copy(noUrutKlg = event.noUrutKlg)
            }

            is IsiRutaScreenEvent.NamaKKChanged -> {
                state = state.copy(namaKK = event.namaKK)
            }

            is IsiRutaScreenEvent.AlamatChanged -> {
                state = state.copy(alamat = event.alamat)
            }

            is IsiRutaScreenEvent.IsGenzOrtuChanged -> {
                state = state.copy(isGenzOrtu = event.isGenzOrtu)
            }

            is IsiRutaScreenEvent.NoUrutKlgEgbChanged -> {
                state = state.copy(noUrutKlgEgb = event.noUrutKlgEgb)
            }

            is IsiRutaScreenEvent.PenglMknChanged -> {
                state = state.copy(penglMkn = event.penglMkn)
            }

            is IsiRutaScreenEvent.NoUrutRutaChanged -> {
                state = state.copy(noUrutRuta = event.noUrutRuta)
            }

            is IsiRutaScreenEvent.KKOrKRTChanged -> {
                state = state.copy(kkOrKrt = event.kkOrKRT)
            }

            is IsiRutaScreenEvent.NamaKRTChanged -> {
                state = state.copy(namaKrt = event.namaKRT)
            }

            is IsiRutaScreenEvent.GenzOrtuChanged -> {
                state = state.copy(genzOrtu = event.genzOrtu)
            }

            is IsiRutaScreenEvent.KatGenzChanged -> {
                state = state.copy(katGenz = event.katGenz)
            }

            is IsiRutaScreenEvent.submit -> {
                val keluarga = Keluarga(
                    SLS = state.SLS,
                    noSegmen = state.noSegmen,
                    noBgFisik = state.noBgFisik.toInt(),
                    noBgSensus = state.noBgSensus.toInt(),
                    noUrutKlg = state.noUrutKlg,
                    namaKK = state.namaKK,
                    alamat = state.alamat,
                    isGenzOrtu = state.isGenzOrtu,
                    noUrutKlgEgb = state.noUrutKlgEgb,
                    penglMkn = state.penglMkn,
                    noBS = noBS,
                    kodeKlg = noBS + state.noUrutKlg,
                )
                state.penglMkn?.let {
                    repeat(it) {
                        val ruta = Ruta(
                            noUrutRuta = state.noUrutRuta,
                            kkOrKrt = state.kkOrKrt,
                            namaKrt = state.namaKrt,
                            isGenzOrtu = state.genzOrtu.toInt(),
                            katGenz = state.katGenz,
                            long = long,
                            lat = lat,
                            kodeRuta = noBS + state.noUrutRuta,
                            noBS = noBS
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