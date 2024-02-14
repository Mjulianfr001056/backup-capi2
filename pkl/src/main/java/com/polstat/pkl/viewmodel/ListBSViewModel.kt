package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.mapper.toKeluarga
import com.polstat.pkl.mapper.toWilayah
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.model.response.FinalisasiBSResponse
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.MahasiswaRepository
import com.polstat.pkl.repository.RemoteRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
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
    private val localRutaRepository: LocalRutaRepository,
    private val remoteRutaRepository: RemoteRutaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_LISTBS_VM"
    }

    private val session = sessionRepository.getActiveSession()

    val isMonitoring = savedStateHandle.get<Boolean>("isMonitoring")

    private val _listWilayah = MutableStateFlow<List<WilayahEntity>>(emptyList())

    val listWilayah = _listWilayah.asStateFlow()

    private val _wilayah = MutableStateFlow(WilayahEntity())

    val wilayah = _wilayah.asStateFlow()

    private val _finalisasiBSResponse = MutableStateFlow(FinalisasiBSResponse())

    val finalisasiBSResponse = _finalisasiBSResponse.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    private val _successMessage = MutableStateFlow("")

    val successMessage = _successMessage.asStateFlow()

    private val _showSuccessToastChannel = Channel<Boolean>()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    val showSuccessToastChannel = _showSuccessToastChannel.receiveAsFlow()

    private val _deleteWilayah = MutableStateFlow(WilayahEntity())

    private val _isSuccesed = MutableStateFlow(false)

    val isSuccesed = _isSuccesed.asStateFlow()

    init {
        getAllWilayah()
        Log.d(TAG, "isMonitoring: $isMonitoring")
        _isSuccesed.value = true
    }

    private fun getAllWilayah() {
        viewModelScope.launch {
            wilayahRepository.getAllWilayah().collectLatest { result ->
                when (result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getAllWilayah: Error in getAllWilayah ($errorMessage)")
                            _isSuccesed.value = true
                        }
                        _showErrorToastChannel.send(true)
                    }

                    is Result.Loading -> Log.d(TAG, "getAllWilayah: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listWilayah.value = it
                            Log.d(TAG, "getAllWilayah: $listWilayah")
                            _isSuccesed.value = true
                        }
                    }
                }
            }
        }
    }

    fun finalisasiBS(
        idBS: String
    ) {
        viewModelScope.launch {
            _isSuccesed.value = false
            val finalisasiBSJob = async {
                remoteRutaRepository.finalisasiBS(idBS).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            result.data?.let { response ->
                                _finalisasiBSResponse.value = response
                                Log.d(TAG, "finalisasiBS succeed: $response")
                                deleteWilayah(idBS)
                                _successMessage.value = "Berhasil melakukan finalisasi blok sensus!"
                                _showSuccessToastChannel.send(true)
                                _isSuccesed.value = true
                            }
                        }

                        is Result.Loading -> Log.d(TAG, "finalisasiBS: Loading...")

                        is Result.Error -> {
                            result.message?.let { error ->
                                _errorMessage.value = error
                            }
                            _showErrorToastChannel.send(true)
                            Log.e(TAG, "finalisasiBS: Error in finalisasiBS (${errorMessage.value})")
                        }
                    }
                }
            }
            finalisasiBSJob.await()

            wilayahRepository.insertWilayah(_finalisasiBSResponse.value.toWilayah()).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    fun generateSampel(idBS: String) {
        viewModelScope.launch {
            _isSuccesed.value = false
            val generateRutaJob = async {
                remoteRutaRepository.generateRuta(idBS).collectLatest { message ->
                    _successMessage.value = message
                    _showSuccessToastChannel.send(true)
                    Log.d(TAG, message)
                }
            }
            generateRutaJob.await()

            if (successMessage.value == "Berhasil Ambil Sampel!") {
                getWilayah(idBS)
                Log.d(TAG, "generateSampel: status BS berhasil diupdate!")
            } else {
                Log.d(TAG, "generateSampel: status BS gagal diupdate!")
                _isSuccesed.value = true
            }
        }
    }

    suspend fun getWilayah(idBS: String) {
        viewModelScope.launch (Dispatchers.IO) {
            wilayahRepository.getWilayah(idBS).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getWilayah: Error in getWilayah (${errorMessage.value})")
                            _isSuccesed.value = true
                        }
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Loading -> Log.d(TAG, "getWilayah: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _wilayah.value = it
                            _successMessage.value = "Berhasil mendapatkan wilayah!"
                            _showSuccessToastChannel.send(true)
                            Log.d(TAG, "getWilayah succeed: ${wilayah.value}")

                            updateWilayah(it, "telah-disampel")
                            _isSuccesed.value = true
                        }
                    }
                }
            }
        }
    }

    suspend fun updateWilayah(wilayahEntity: WilayahEntity, statusBS: String) {
        val updatedWilayah = wilayahEntity.copy(status = statusBS)
        viewModelScope.launch {
            wilayahRepository.updateWilayah(updatedWilayah).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    suspend fun deleteWilayah(idBS: String) {
        viewModelScope.launch {
//            val getWilayahJob = async {
//                wilayahRepository.getWilayah(idBS).collectLatest { result ->
//                    when(result) {
//                        is Result.Error -> {
//                            result.message?.let { error ->
//                                _errorMessage.value = error
//                            }
//                        }
//                        is Result.Loading -> Log.d(TAG, "getWilayah: Loading...")
//                        is Result.Success -> {
//                            result.data?.let {
//                                _deleteWilayah.value = it
//                                Log.d(TAG, "deleteWilayah: Berhasil mendapatkan wilayah! ${_deleteWilayah.value}")
//                            }
//                        }
//                    }
//                }
//            }
//            getWilayahJob.await()

//            Log.d(TAG, "deleteWilayah: fakeDeleteKeluarga ${_deleteWilayah.value.toWilayah}")
//            wilayahRepository.fakeDeleteWilayah(_deleteWilayah.value.toWilayah()).collectLatest { message ->
//                Log.d(TAG, message)
//            }
            wilayahRepository.deleteWilayah(idBS).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    fun updateShowLoading(isSuccesed: Boolean) {
        _showLoadingChannel.trySend(!isSuccesed)
    }
}