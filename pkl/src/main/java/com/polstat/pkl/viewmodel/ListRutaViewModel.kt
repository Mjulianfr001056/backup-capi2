package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.relation.WilayahWithAll
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.mapper.toKeluargaDto
import com.polstat.pkl.mapper.toRuta
import com.polstat.pkl.mapper.toRutaDtoList
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.request.JsonKlg
import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.SyncRutaResponse
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.RemoteRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListRutaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val wilayahRepository: WilayahRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val remoteRutaRepository: RemoteRutaRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    companion object {
        private const val TAG = "CAPI63_LISTRUTA_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    val noBS = savedStateHandle.get<String>("noBS")

    private val _wilayahWithRuta = MutableStateFlow(WilayahWithRuta())

    val wilayahWithRuta = _wilayahWithRuta.asStateFlow()

    private val _wilayahWithAll = MutableStateFlow(WilayahWithAll())

    val wilayahWithAll = _wilayahWithAll.asStateFlow()

    private val _synchronizeRuta = MutableStateFlow(SyncRutaResponse())

    private val _deleteRuta = MutableStateFlow(Ruta())

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        getWilayahWithAll(noBS!!)
    }

//    fun searchRuta(search: String) {
//        if (!::listRuta.isInitialized)
//            return
//
//        if (rutaUiState is RutaUiState.Success) {
//            val filteredRuta = listRuta.filter { listRuta ->
//                listRuta.kodeRuta.lowercase().contains(search.lowercase(), false)
//                listRuta.namaKrt.toString().lowercase().contains(search.lowercase(), false)
//            }
//            rutaUiState = RutaUiState.Success(filteredRuta)
//        }
//    }

    private fun getWilayahWithAll(
        noBS: String
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
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getWilayahWithAll: Error in getWilayahWithAll")
                    }
                }
            }
        }
    }

    private fun getWilayahWithRuta(
        noBS: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithRuta(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _wilayahWithRuta.value = response
                            Log.d(TAG, "getWilayahWithRuta success: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getWilayahWithRuta: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getWilayahWithRuta: Error in getWilayahWithRuta")
                    }
                }
            }
        }
    }

    fun synchronizeRuta(
        wilayahWithAll: WilayahWithAll,
        nim: String,
        noBS: String
    ) {
        val jsonKlgInstance = JsonKlg()

        if (wilayahWithAll.listKeluargaWithRuta!!.isNotEmpty()) {
            wilayahWithAll.listKeluargaWithRuta.forEach{ keluargaWithRuta ->
                jsonKlgInstance.add(keluargaWithRuta.keluarga.toKeluargaDto(keluargaWithRuta.listRuta.toRutaDtoList()))
            }
        }

        val syncRutaRequest = SyncRutaRequest(
            nim = nim,
            no_bs = noBS,
            json = jsonKlgInstance
        )
        viewModelScope.launch {
            remoteRutaRepository.sinkronisasiRuta(syncRutaRequest).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _synchronizeRuta.value = response
                            Log.d(
                                TAG, "synchronizeRuta succeed: $response"
                            )
                        }
                    }

                    is Result.Loading -> {
                        Log.d(
                            TAG,
                            "synchronizeRuta: Loading..."
                        )
                    }

                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(
                            TAG,
                            "synchronizeRuta: Error in synchronizeRuta"
                        )
                    }

                }
            }
        }
    }

    private fun deleteRuta(
        kodeRuta: String
    ) {
        viewModelScope.launch {
            localRutaRepository.getRuta(kodeRuta).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _deleteRuta.value = response.toRuta()
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

        viewModelScope.launch {
            localRutaRepository.fakeDeleteRuta(_deleteRuta.value).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }
}

sealed interface RutaUiState {
    data class Success(val proker: List<Ruta>) : RutaUiState
    object Error : RutaUiState
    object Loading : RutaUiState
}