package com.polstat.pkl.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.mapper.toKeluarga
import com.polstat.pkl.mapper.toRuta
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.ui.event.EditRutaEvent
import com.polstat.pkl.ui.state.EditRutaState
import com.polstat.pkl.ui.state.IsiRutaScreenState
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
class EditRutaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val keluargaRepository: KeluargaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_EDITRUTA_VM"
    }

    val noBS = savedStateHandle.get<String>("noBS")

    val kodeRuta = savedStateHandle.get<String>("kodeRuta")

    val kodeKlg = savedStateHandle.get<String>("kodeKlg")

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _state = MutableStateFlow(EditRutaState())

    val state = _state.asStateFlow()

    private val _editRuta = MutableStateFlow(Ruta())

    val editRuta = _editRuta.asStateFlow()

    private val _editKlg = MutableStateFlow(Keluarga())

    val editKlg = _editKlg.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        Log.d(TAG, "noBS: $noBS")
        Log.d(TAG, "kodeRuta: $kodeRuta")
        Log.d(TAG, "kodeKlg: $kodeKlg")

        viewModelScope.launch {
            val job = launch {
                getRuta(kodeRuta!!)
                getKeluarga(kodeKlg!!)
            }
            job.join()
            delay(2000)
            setExistingValue()
        }

    }

    fun setExistingValue() {
        _state.value = state.value.copy(
            SLS = editKlg.value.SLS,
            noSegmen = editKlg.value.noSegmen,
            noBgFisik = editKlg.value.noBgFisik,
            noBgSensus = editKlg.value.noBgSensus,
            noUrutKlg = editKlg.value.noUrutKlg,
            namaKK = editKlg.value.namaKK,
            alamat = editKlg.value.alamat,
            isGenzOrtu = editKlg.value.isGenzOrtu,
            noUrutKlgEgb = editKlg.value.noUrutKlgEgb,
            penglMkn = editKlg.value.penglMkn,
            noUrutRuta = editRuta.value.noUrutRuta,
            kkOrKrt = if (editRuta.value.kkOrKrt == "1") "Kepala Keluarga (KK) saja" else if (editRuta.value.kkOrKrt == "2") "Kepala Rumah Tangga (KRT) saja" else "KK Sekaligus KRT",
            namaKrt = editRuta.value.namaKrt,
            genzOrtu = editRuta.value.isGenzOrtu,
            katGenz = editRuta.value.katGenz,
            long = editRuta.value.long,
            lat = editRuta.value.lat
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
                            _editRuta.value = response.toRuta()
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

    fun getKeluarga(
        kodeKlg: String
    ) {
        viewModelScope.launch {
            keluargaRepository.getKeluarga(kodeKlg).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _editKlg.value = response.toKeluarga()
                            Log.d(
                                TAG, "getKeluarga succeed: $response"
                            )
                        }
                    }

                    is Result.Loading -> {
                        Log.d(
                            TAG, "getKeluarga: Loading..."
                        )
                    }

                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(
                            TAG, "getKeluarga: Error in getKeluarga"
                        )
                    }
                }
            }
        }
    }

    private fun updateRuta(
        ruta: Ruta
    ) {
        viewModelScope.launch {
            localRutaRepository.updateRuta(ruta).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    private fun updateKeluarga(
        keluarga: Keluarga
    ) {
        viewModelScope.launch {
            keluargaRepository.updateKeluarga(keluarga).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun onEvent(
        event: EditRutaEvent,
        index: Int = 0
    ) {
        when(event) {
            is EditRutaEvent.SLSChanged -> {
                _state.emit(state.value.copy(SLS = event.sls))
            }
            is EditRutaEvent.NoSegmenChanged -> {
                _state.emit(state.value.copy(noSegmen = event.noSegmen))
            }
            is EditRutaEvent.NoBgFisikChanged -> {
                _state.emit(state.value.copy(noBgFisik = event.noBgFisik))
            }
            is EditRutaEvent.NoBgSensusChanged -> {
                _state.emit(state.value.copy(noBgSensus = event.noBgSensus))
            }
            is EditRutaEvent.NoUrutKlgChanged -> {
                _state.emit(state.value.copy(noUrutKlg = event.noUrutKlg))
            }
            is EditRutaEvent.NamaKKChanged -> {
                _state.emit(state.value.copy(namaKK = event.namaKK))
            }
            is EditRutaEvent.AlamatChanged -> {
                _state.emit(state.value.copy(alamat = event.alamat))
            }
            is EditRutaEvent.IsGenzOrtuChanged -> {
                _state.emit(state.value.copy(isGenzOrtu = event.isGenzOrtu))
            }
            is EditRutaEvent.NoUrutKlgEgbChanged -> {
                _state.emit(state.value.copy(noUrutKlgEgb = event.noUrutKlgEgb))
            }
            is EditRutaEvent.PenglMknChanged -> {
                _state.emit(state.value.copy(penglMkn = event.penglMkn))
            }
            is EditRutaEvent.NoUrutRutaChanged -> {
                _state.emit(state.value.copy(noUrutRuta = event.noUrutRuta))
            }
            is EditRutaEvent.KKOrKRTChanged -> {
                _state.emit(state.value.copy(kkOrKrt = event.kkOrKRT))
            }
            is EditRutaEvent.NamaKRTChanged -> {
                _state.emit(state.value.copy(namaKrt = event.namaKRT))
            }
            is EditRutaEvent.GenzOrtuChanged -> {
                _state.emit(state.value.copy(genzOrtu = event.genzOrtu))
            }
            is EditRutaEvent.KatGenzChanged -> {
                _state.emit(state.value.copy(katGenz = event.katGenz))
            }
            is EditRutaEvent.submit -> {
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
                    status = "update"
                )

                updateKeluarga(keluarga)

                val ruta = Ruta(
                    kodeRuta = "R" + noBS + UtilFunctions.convertTo3DigitsString(state.value.noUrutRuta!!),
                    noUrutRuta = state.value.noUrutRuta!!,
                    noUrutEgb = 0,
                    kkOrKrt = state.value.kkOrKrt!!,
                    namaKrt = state.value.namaKrt!!,
                    isGenzOrtu = state.value.genzOrtu!!,
                    katGenz = if (state.value.genzOrtu!! >= 1 && state.value.genzOrtu!! <= 2) 1 else if (state.value.genzOrtu!! >= 3 && state.value.genzOrtu!! <= 4) 2 else if (state.value.genzOrtu!! > 4) 3 else 0,
                    long = state.value.long!!,
                    lat = state.value.lat!!,
                    noBS = noBS,
                    status = "update"
                )

                updateRuta(ruta)

            }
        }
    }

}