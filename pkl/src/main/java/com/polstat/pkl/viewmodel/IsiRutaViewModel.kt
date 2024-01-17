package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        when(event){
            is IsiRutaScreenEvent.NoSegmenChanged -> {
                state = state.copy(noSegmen = event.noSegmen)
            }
            is IsiRutaScreenEvent.NoBgFisikChanged -> {
                state = state.copy(noBgFisik = event.noBgFisik)
            }
            is IsiRutaScreenEvent.NoBgSensusChanged -> {
                state = state.copy(noBgSensus = event.noBgSensus)
            }
            is IsiRutaScreenEvent.NoUrutRutaChanged -> {
                state = state.copy(noUrutRuta = event.noUrutRuta)
            }
            is IsiRutaScreenEvent.NamaKrtChanged -> {
                state = state.copy(namaKrt = event.namaKrt)
            }
            is IsiRutaScreenEvent.AlamatChanged -> {
                state = state.copy(alamat = event.alamat)
            }
            is IsiRutaScreenEvent.IsGenzOrtuChanged -> {
                state = state.copy(isGenzOrtu = event.isGenzOrtu)
            }
            is IsiRutaScreenEvent.JmlGenzChanged -> {
                state = state.copy(jmlGenz = event.jmlGenz)
            }
            is IsiRutaScreenEvent.NoUrutRtEgbChanged -> {
                state = state.copy(noUrutRtEgb = event.noUrutRtEgb)
            }
            is IsiRutaScreenEvent.CatatanChanged -> {
                state = state.copy(catatan = event.catatan)
            }
            is IsiRutaScreenEvent.submit -> {
                val ruta = Ruta(
                    noSegmen = state.noSegmen.toInt(),
                    noBgFisik = state.noBgFisik.toInt(),
                    noBgSensus = state.noBgSensus.toInt(),
                    noUrutRuta = state.noUrutRuta.toInt(),
                    namaKrt = state.namaKrt,
                    alamat = state.alamat,
                    isGenzOrtu = convertIsGenzOrtu(state.isGenzOrtu),
                    jmlGenz = state.jmlGenz.toInt(),
                    noUrutRtEgb = state.noUrutRtEgb.toInt(),
                    catatan = state.catatan,
                    noBS = noBS,
                    kodeRuta = noBS + state.noUrutRuta,
                    lat = lat,
                    long = long
                )
                Log.d(TAG, "ruta : $ruta")
                insertRuta(ruta)
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