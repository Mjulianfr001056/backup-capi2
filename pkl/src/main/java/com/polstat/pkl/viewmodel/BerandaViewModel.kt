package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.relation.DataTimWithAll
import com.polstat.pkl.repository.DataTimRepository
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
class BerandaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val dataTimRepository: DataTimRepository
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_BERANDA_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _dataTimWithAll = MutableStateFlow(DataTimWithAll())

    val dataTimWithAll = _dataTimWithAll.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {

        getDataTimWithAll(_session!!.idTim!!)

    }

    private fun getDataTimWithAll(
        idTim: String
    ) {
        viewModelScope.launch {
            dataTimRepository.getDataTimWithAll(idTim).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _dataTimWithAll.value = response
                            Log.d(TAG, "getDataTimWithAll succeed: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getDataTimWithAll: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getDataTimWithAll: Error in getDataTimWithAll")
                    }

                }
            }
        }
    }

}