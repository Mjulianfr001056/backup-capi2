package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.model.response.SampelRutaResponse
import com.polstat.pkl.repository.SampelRutaRepository
import com.polstat.pkl.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListSampelViewModel @Inject constructor(
    private val sampelRutaRepository: SampelRutaRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_LISTSAMPEL_VM"
    }

    private val sampelRutaValue = SampelRutaResponse()

    private val _listSampelRutaByBS = MutableStateFlow(sampelRutaValue)

    val listSampelRutaByBS = _listSampelRutaByBS.asStateFlow()

    private val _listSampelRuta = MutableStateFlow<List<SampelRutaEntity>>(emptyList())

    val listSampelRuta = _listSampelRuta.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _session = sessionRepository.getActiveSession()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        getSampelByBS("444B")
    }

    fun fetchSampelRuta(
        noBS: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            openLoadingDialog()
            sampelRutaRepository.getSampelRutaFromWS(noBS).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            Log.d(TAG, "$response")

                            _listSampelRutaByBS.value = response

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

            if (listSampelRutaByBS.value.SampelRuta.isNotEmpty() == true) {
                listSampelRutaByBS.value.SampelRuta.forEach { sampelRuta ->
                    sampelRutaRepository.insertSampelRuta(sampelRuta).collectLatest { message ->
                        Log.d(TAG, message)
                    }
                }
            }
        }
    }

    fun getSampelByBS(
        noBS: String
    ) {
        viewModelScope.launch {
            sampelRutaRepository.getSampelRuta(noBS).collectLatest { result ->
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

    private fun openLoadingDialog() {
        _showLoadingChannel.trySend(true)
    }

    private fun closeLoadingDialog() {
        _showLoadingChannel.trySend(false)
    }
}