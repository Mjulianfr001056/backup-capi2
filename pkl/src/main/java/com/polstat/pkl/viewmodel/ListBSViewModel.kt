package com.polstat.pkl.viewmodel

import android.util.Log
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
    private val mahasiswaRepository: MahasiswaRepository
) : ViewModel() {

    companion object{
        private  const val TAG = "CAPI63_LISTBS_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _listWilayahByNIM = MutableStateFlow<List<WilayahEntity>>(emptyList())

    val listWilayahByNIM = _listWilayahByNIM.asStateFlow()

    private val _mahasiswaWithWilayah = MutableStateFlow(MahasiswaWithWilayah())

    val mahasiswaWithWilayah = _mahasiswaWithWilayah.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {

//        getWilayahByNIM(_session!!.nim!!)

        getMahasiswaWithWilayah(_session!!.nim!!)
    }

    private fun getWilayahByNIM(
        nim: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahByNIM(nim).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _listWilayahByNIM.value = response
                            Log.d(TAG, "getDataWilayahByNIM success: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getDataWilayahByNIM: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getDataWilayahByNIM: Error in getDataWilayahByNIM")
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