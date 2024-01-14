package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.model.domain.Ruta
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
    private val remoteRutaRepository: RemoteRutaRepository
): ViewModel() {

    companion object {
        private const val TAG = "CAPI63_LISTRUTA_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _listWilayahWithRuta = MutableStateFlow(WilayahWithRuta())

    val listWilayahWithRuta = _listWilayahWithRuta.asStateFlow()

    private val _synchronizeRuta = MutableStateFlow(SyncRutaResponse())

    val synchronizeRuta = _synchronizeRuta.asStateFlow()

    private val _editRuta = MutableStateFlow("")

    val editRuta = _editRuta.asStateFlow()



    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        getWilayahWithRuta("444B")
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

    private fun getWilayahWithRuta(
        noBS: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithRuta(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _listWilayahWithRuta.value = response
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

    private fun synchronizeRuta(
        syncRutaRequest: SyncRutaRequest
    ) {
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

//    private fun editRuta(
//        ruta: Ruta
//    ) {
//        viewModelScope.launch {
//            localRutaRepository.updateRuta(ruta).collectLatest { result ->
//            when (result) {
//                    is Result.Success -> {
//                        result.data?.let { response ->
//                            _editRuta.value = response
//                            Log.d(
//                                TAG, "editRuta succeed: $response"
//                            )
//                        }
//                    }
//
//                    is Result.Loading -> {
//                        Log.d(
//                            TAG, "editRuta: Loading..."
//                        )
//                    }
//
//                    is Result.Error -> {
//                        result.message?.let { error ->
//                            _errorMessage.value = error
//                        }
//                        _showErrorToastChannel.send(true)
//                        Log.e(
//                            TAG, "editRuta: Error in editRuta"
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    private fun deleteRuta(
//        ruta: Ruta
//    ) {
//        viewModelScope.launch {
//            localRutaRepository.fakeDeleteRuta(ruta).collectLatest { result ->
//                when (result) {
//                    is Result.Success -> {
//                        result.data?.let { response ->
//                            _editRuta.value = response
//                            Log.d(
//                                TAG, "deleteRuta succeed: $response"
//                            )
//                        }
//                    }
//
//                    is Result.Loading -> {
//                        Log.d(
//                            TAG, "deleteRuta: Loading..."
//                        )
//                    }
//
//                    is Result.Error -> {
//                        result.message?.let { error ->
//                            _errorMessage.value = error
//                        }
//                        _showErrorToastChannel.send(true)
//                        Log.e(
//                            TAG, "deleteRuta: Error in deleteRuta"
//                        )
//                    }
//                }
//            }
//        }
//    }
}

sealed interface RutaUiState {
    data class Success(val proker: List<Ruta>) : RutaUiState
    object Error : RutaUiState
    object Loading : RutaUiState
}