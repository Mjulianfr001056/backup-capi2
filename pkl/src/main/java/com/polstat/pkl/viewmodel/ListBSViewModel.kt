package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.MahasiswaWithWilayah
import com.polstat.pkl.repository.MahasiswaRepository
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
class ListBSViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val wilayahRepository: WilayahRepository,
    private val mahasiswaRepository: MahasiswaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object{
        private  const val TAG = "CAPI63_LISTBS_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    val isMonitoring = savedStateHandle.get<Boolean>("isMonitoring")

    private val _listWilayah = MutableStateFlow<List<WilayahEntity>>(emptyList())

    val listWilayah = _listWilayah.asStateFlow()

    private val _listWilayahByNIM = MutableStateFlow<List<WilayahEntity>>(emptyList())

    val listWilayahByNIM = _listWilayahByNIM.asStateFlow()

    private val _mahasiswaWithWilayah = MutableStateFlow(MahasiswaWithWilayah())

    val mahasiswaWithWilayah = _mahasiswaWithWilayah.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        getAllWilayah()
        Log.d(TAG, "isMonitoring: $isMonitoring")
    }

    private fun getAllWilayah() {
        viewModelScope.launch {
            wilayahRepository.getAllWilayah().collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getAllWilayah: Error in getAllWilayah ($errorMessage)")
                        }
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Loading -> Log.d(TAG, "getAllWilayah: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listWilayah.value = it
                            Log.d(TAG, "getAllWilayah: $listWilayah")
                        }
                    }
                }
            }
        }
    }

    private fun getMahasiswaWithWilayah(
        nim: String
    ) {
        viewModelScope.launch {
            mahasiswaRepository.getMahasiswaWithWilayah(nim).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _mahasiswaWithWilayah.value = response
                            Log.d(TAG, "getMahasiswaWithWilayah success: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getMahasiswaWithWilayah: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getMahasiswaWithWilayah: Error in getMahasiswaWithWilayah")
                    }
                }
            }
        }
    }
}