package com.polstat.pkl.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.relation.DataTimWithAll
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.repository.DataTimRepository
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
class BerandaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val dataTimRepository: DataTimRepository,
    private val wilayahRepository: WilayahRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_BERANDA_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _dataTimWithAll = MutableStateFlow(DataTimWithAll())

    val dataTimWithAll = _dataTimWithAll.asStateFlow()

    private val _wilayahWithRuta = MutableStateFlow(WilayahWithRuta())

    val wilayahWithRuta = _wilayahWithRuta.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        try {
            getDataTimWithAll(_session!!.idTim!!)

            val allValues: Map<String, *> = sharedPreferences.all
            for ((key, value) in allValues) {
                Log.d(TAG, "Prefences -> $key : $value")
            }
        } catch (e: Exception) {
            Log.e(TAG, "init: Error in init", e)
        }
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

    fun getWilayahWithRuta(
        noBS: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithRuta(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _wilayahWithRuta.value = response
                            Log.d(TAG, "getWilayahWithRuta succeed: $response")
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

    fun logout() {
        viewModelScope.launch {
            sessionRepository.clearSession()
        }
    }

}