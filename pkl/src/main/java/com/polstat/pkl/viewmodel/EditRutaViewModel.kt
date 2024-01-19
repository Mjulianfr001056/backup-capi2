package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.mapper.toRuta
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.SessionRepository
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
class EditRutaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_EDITRUTA_VM"
    }

    val kodeRuta = savedStateHandle.get<String>("kodeRuta")

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _editRuta = MutableStateFlow(Ruta())

    val editRuta = _editRuta.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()


    private fun editRuta(
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

        viewModelScope.launch {
            localRutaRepository.updateRuta(_editRuta.value).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }
}