package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.relation.WilayahWithAll
import com.polstat.pkl.mapper.toRuta
import com.polstat.pkl.mapper.toWilayah
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.ui.event.SalinRutaEvent
import com.polstat.pkl.ui.state.SalinRutaState
import com.polstat.pkl.utils.Result
import com.polstat.pkl.utils.UtilFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalinRutaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val keluargaRepository: KeluargaRepository,
    private val wilayahRepository: WilayahRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    companion object {
        private const val TAG = "CAPI63_SalinRuta_VM"
    }

    val noBS = savedStateHandle.get<String>("noBS").orEmpty()

    val kodeRuta = savedStateHandle.get<String>("kodeRuta")

    val kodeKlg = savedStateHandle.get<String>("kodeKlg")

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _state = MutableStateFlow(SalinRutaState())

    val state = _state.asStateFlow()

    private val _salinRuta = MutableStateFlow(Ruta())

    val salinRuta = _salinRuta.asStateFlow()

//    private val _klgBaru = MutableStateFlow(Keluarga())
//
//    val klgBaru = _klgBaru.asStateFlow()

    private val _wilayahWithAll = MutableStateFlow(WilayahWithAll())

    val wilayahWithAll = _wilayahWithAll.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        Log.d(TAG, "noBS: $noBS")
        Log.d(TAG, "kodeRuta: $kodeRuta")

        viewModelScope.launch {
            getRuta(kodeRuta!!)
            delay(2000)
            setExistingValue()
        }

    }

    fun setExistingValue() {
        _state.value = state.value.copy(
            noUrutRuta = salinRuta.value.noUrutRuta,
            kkOrKrt = if (salinRuta.value.kkOrKrt == "1") "Kepala Keluarga (KK) saja" else if (salinRuta.value.kkOrKrt == "2") "Kepala Rumah Tangga (KRT) saja" else "KK Sekaligus KRT",
            namaKrt = salinRuta.value.namaKrt,
            genzOrtu = salinRuta.value.isGenzOrtu,
            katGenz = salinRuta.value.katGenz,
            long = salinRuta.value.long,
            lat = salinRuta.value.lat
        )
        Log.d(TAG, "setExistingValue: ${state.value}")
    }

    fun getRuta(
        kodeRuta: String
    ) {
        viewModelScope.launch {
            localRutaRepository.getRuta(kodeRuta).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _salinRuta.value = response.toRuta()
                            Log.d(
                                TAG, "getRuta succeed: $response"
                            )
                        }
                    }

                    is Result.Loading -> {
                        Log.d(
                            TAG, "getRuta: Loading..."
                        )
                    }

                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(
                            TAG, "getRuta: Error in getRuta"
                        )
                    }
                }
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
//            wilayahRepository.updateWilayah(updatedWilayah, nim).collectLatest { message ->
//                Log.d(TAG, message)
//            }
            wilayahRepository.updateWilayah(updatedWilayah).collectLatest { message ->
                Log.d(TAG, message)
            }
        }

    }

//    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun onEvent(
        event: SalinRutaEvent,
        index: Int = 0
    ) {
        when(event) {
            is SalinRutaEvent.SLSChanged -> {
                _state.emit(state.value.copy(SLS = event.sls))
            }
            is SalinRutaEvent.NoSegmenChanged -> {
                _state.emit(state.value.copy(noSegmen = event.noSegmen))
            }
            is SalinRutaEvent.NoBgFisikChanged -> {
                _state.emit(state.value.copy(noBgFisik = event.noBgFisik))
            }
            is SalinRutaEvent.NoBgSensusChanged -> {
                _state.emit(state.value.copy(noBgSensus = event.noBgSensus))
            }
            is SalinRutaEvent.NoUrutKlgChanged -> {
                _state.emit(state.value.copy(noUrutKlg = event.noUrutKlg))
            }
            is SalinRutaEvent.NamaKKChanged -> {
                _state.emit(state.value.copy(namaKK = event.namaKK))
            }
            is SalinRutaEvent.AlamatChanged -> {
                _state.emit(state.value.copy(alamat = event.alamat))
            }
            is SalinRutaEvent.IsGenzOrtuChanged -> {
                _state.emit(state.value.copy(isGenzOrtu = event.isGenzOrtu))
            }
            is SalinRutaEvent.NoUrutKlgEgbChanged -> {
                _state.emit(state.value.copy(noUrutKlgEgb = event.noUrutKlgEgb))
            }
            is SalinRutaEvent.PenglMknChanged -> {
                _state.emit(state.value.copy(penglMkn = event.penglMkn))
            }
            is SalinRutaEvent.NoUrutRutaChanged -> {
                _state.emit(state.value.copy(noUrutRuta = event.noUrutRuta))
            }
            is SalinRutaEvent.KKOrKRTChanged -> {
                _state.emit(state.value.copy(kkOrKrt = event.kkOrKRT))
            }
            is SalinRutaEvent.NamaKRTChanged -> {
                _state.emit(state.value.copy(namaKrt = event.namaKRT))
            }
            is SalinRutaEvent.GenzOrtuChanged -> {
                _state.emit(state.value.copy(genzOrtu = event.genzOrtu))
            }
            is SalinRutaEvent.KatGenzChanged -> {
                _state.emit(state.value.copy(katGenz = event.katGenz))
            }
            is SalinRutaEvent.submit -> {
                val keluarga = Keluarga(
                    SLS = state.value.SLS,
                    noSegmen = state.value.noSegmen,
                    noBgFisik = state.value.noBgFisik,
                    noBgSensus = state.value.noBgSensus,
                    noUrutKlg = state.value.noUrutKlg.toString(),
                    namaKK = state.value.namaKK,
                    alamat = state.value.alamat,
                    isGenzOrtu = state.value.isGenzOrtu,
                    noUrutKlgEgb = state.value.noUrutKlgEgb,
                    penglMkn = state.value.penglMkn,
//                    noBS = noBS,
                    kodeKlg = "K"+ noBS + UtilFunctions.convertTo3DigitsString(state.value.noUrutKlg!!),
                    status = "insert"
                )

                insertKeluarga(keluarga)

                val kodeRuta = "R" + noBS + UtilFunctions.convertTo3DigitsString(state.value.noUrutRuta!!)

                insertKeluargaAndRuta(keluarga.kodeKlg, kodeRuta)

                updateRekapitulasiWilayah(
                    noBS = noBS!!,
                    nim = session!!.nim!!
                )

            }
        }
    }


}