package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.model.response.SampelRutaResponse
import com.polstat.pkl.repository.SampelRutaRepository
import com.polstat.pkl.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListSampelViewModel @Inject constructor(
    private val sampelRutaRepository: SampelRutaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_LISTSAMPEL_VM"
    }

    val idBS = savedStateHandle.get<String>("idBS")

    val isMonitoring = savedStateHandle.get<Boolean>("isMonitoring")

    private val _sampelRutaResponse = MutableStateFlow(SampelRutaResponse())

    val sampelRutaResponse = _sampelRutaResponse.asStateFlow()

    private val _listSampelRuta = MutableStateFlow<List<SampelRutaEntity>>(emptyList())

    val listSampelRuta = _listSampelRuta.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    private val _showSuccessToastChannel = Channel<Boolean>()

    val showSuccessToastChannel = _showSuccessToastChannel.receiveAsFlow()

    private val _successMessage = MutableStateFlow("")

    val successMessage = _successMessage.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)

    val isSyncing: StateFlow<Boolean> get() = _isSyncing

    private val _isDataInserted = MutableStateFlow(false)

    val isDataInserted = _isDataInserted.asStateFlow()

    init {
        viewModelScope.launch {
            idBS?.let {
                val getSampelByBSFromDbJob = async {
                    getSampelByBSFromDB(it)
                }
                getSampelByBSFromDbJob.await()
                if (listSampelRuta.value.isEmpty()) {
                    getSampelRutaFromWSAndInsertThem(it)
                }
            }
        }
    }


    private suspend fun getSampelRutaFromWSAndInsertThem(
        idBS: String
    ) {
        viewModelScope.launch {
            sampelRutaRepository.getSampelRutaFromWS(idBS).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            Log.d(TAG, "$response")

                            _sampelRutaResponse.value = response

                            Log.d(TAG, "Fetch Sampel Ruta successful: $response")
                        }
                    }

                    is Result.Loading -> {
                        Log.d(TAG, "Loading...")
                    }

                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "Error in Fetch Sampel Ruta")
                    }
                }
            }

            if (_sampelRutaResponse.value.isNotEmpty()) {
                _sampelRutaResponse.value.forEach { sampelRuta ->
                    sampelRutaRepository.insertSampelRuta(sampelRuta).collectLatest { message ->
                        Log.d(TAG, message)
                    }
                }
                _isDataInserted.value = true
            }
        }
    }

    suspend fun getSampelByBSFromDB(
        idBS: String
    ) {
        viewModelScope.launch {
            sampelRutaRepository.getSampelRuta(idBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _listSampelRuta.value = response
                            Log.d(TAG, "getSampelByBS success: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getSampelByBS: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getSampelByBS: Error in getSampelByBS")
                    }
                }
            }
        }
    }

    fun confirmSampel(kodeRuta: String) {
        viewModelScope.launch {
            try {
                sampelRutaRepository.confirmSampel(kodeRuta).collectLatest { message ->
                    _successMessage.value = message
                    _showSuccessToastChannel.send(true)
                    Log.d(TAG, message)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}", e)
                _showErrorToastChannel.send(true)
            }
        }
    }

    fun filteredList (searchText: String, listSampelRuta: List<SampelRutaEntity>): List<SampelRutaEntity> {
        val filteredList = listSampelRuta.filter {
            (searchText.isBlank() || it.namaKrt!!.contains(searchText, ignoreCase = true)) || (searchText.isBlank() || it.noUrutRuta.toString()
                .contains(searchText, ignoreCase = true)) || (searchText.isBlank() || it.SLS!!.contains(searchText, ignoreCase = true)) || (searchText.isBlank() || it.alamat!!.contains(searchText, ignoreCase = true)) || (searchText.isBlank() || it.kodeRuta.contains(searchText, ignoreCase = true)) || (searchText.isBlank() || it.noBgFisik!!.contains(searchText, ignoreCase = true)) || (searchText.isBlank() || it.noBgSensus
                ?.contains(searchText, ignoreCase = true)!!) || (searchText.isBlank() || it.noSegmen.toString().contains(searchText, ignoreCase = true)) || (searchText.isBlank() || it.noUrutRutaEgb.toString()
                .contains(searchText, ignoreCase = true))
        }

        return filteredList
    }

    fun sortedListByRutaDescending(filteredList: List<SampelRutaEntity>): List<SampelRutaEntity> {
        return filteredList.sortedByDescending { it.noUrutRuta }
    }

    fun sortedListByRutaAscending(filteredList: List<SampelRutaEntity>): List<SampelRutaEntity> {
        return filteredList.sortedBy { it.noUrutRuta }
    }

    private fun openLoadingDialog() {
        _showLoadingChannel.trySend(true)
    }

    private fun closeLoadingDialog() {
        _showLoadingChannel.trySend(false)
    }

    fun updateShowLoading(isDataInserted: Boolean) {
        _showLoadingChannel.trySend(!isDataInserted)
    }
}