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
import com.polstat.pkl.mapper.toWilayah
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.request.JsonKlg
import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.FinalisasiBSResponse
import com.polstat.pkl.model.response.SyncRutaResponse
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.RemoteRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_LISTRUTA_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    val isMonitoring = savedStateHandle.get<Boolean>("isMonitoring")

    val noBS = savedStateHandle.get<String>("noBS")

    val kodeRuta = savedStateHandle.get<String>("kodeRuta")

    val kodeKlg = savedStateHandle.get<String>("kodeKlg")

    private val _wilayahWithRuta = MutableStateFlow(WilayahWithRuta())

    val wilayahWithRuta = _wilayahWithRuta.asStateFlow()

    private val _wilayahWithAll = MutableStateFlow(WilayahWithAll())

    val wilayahWithAll = _wilayahWithAll.asStateFlow()

    private val _synchronizeRuta = MutableStateFlow(SyncRutaResponse())

    val synchronizeRuta = _synchronizeRuta.asStateFlow()

    private val _finalisasiBSResponse = MutableStateFlow(FinalisasiBSResponse())

    private val _deleteRuta = MutableStateFlow(Ruta())

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

    init {
        getWilayahWithAll(noBS.toString())
        Log.d(TAG, "isMonitoring: $isMonitoring")
    }

    private fun getWilayahWithAll(
        noBS: String
    ) {
        viewModelScope.launch {
            wilayahRepository.getWilayahWithAll(noBS).collectLatest { result ->
                when (result) {
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
                when (result) {
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

//        val wilayah = wilayahWithAll.wilayahWithKeluarga!!.wilayah

//        val updatedWilayah = wilayah!!.copy(status = "listing-selesai")

        if (wilayahWithAll.listKeluargaWithRuta!!.isNotEmpty()) {
            wilayahWithAll.listKeluargaWithRuta.forEach { keluargaWithRuta ->
                if (keluargaWithRuta.keluarga.status != "fetch") {
                    jsonKlgInstance.add(keluargaWithRuta.keluarga.toKeluargaDto(keluargaWithRuta.listRuta.toRutaDtoList()))
                }
            }
        }

        val syncRutaRequest = SyncRutaRequest(
            nim = nim,
            no_bs = noBS,
            json = jsonKlgInstance
        )
        Log.d(TAG, "synchronizeRuta: SyncRutaRequest $syncRutaRequest")
        viewModelScope.launch {
            val job1 = async {
                remoteRutaRepository.sinkronisasiRuta(syncRutaRequest).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            result.data?.let { response ->
                                _synchronizeRuta.value = response
                                _successMessage.value = "Berhasil melakukan sinkronisasi!"
                                _showSuccessToastChannel.send(true)
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

            job1.await()

            wilayahRepository.insertWilayah(synchronizeRuta.value.toWilayah()).collectLatest { message ->
                Log.d(TAG, message)
            }
        }
    }

    fun generateRuta(noBS: String) {
        val wilayah = _wilayahWithAll.value.wilayahWithKeluarga!!.wilayah

        val updatedWilayah = wilayah!!.copy(status = "telah-disampel")

        viewModelScope.launch {

            val job = launch {
                remoteRutaRepository.generateRuta(noBS).collectLatest { message ->
                    _successMessage.value = message
                    _showSuccessToastChannel.send(true)
                    Log.d(TAG, message)
                }
            }

            job.join()

            delay(2000)

            val job2 = launch {
                showErrorToastChannel.collectLatest { isError ->
                    if (!isError) {
                        launch {
                            wilayahRepository.updateWilayah(
                                updatedWilayah.toWilayah(emptyList())
                            )
                                .collectLatest { message ->
                                    Log.d(TAG, message)
                                }
                        }
                    }
                }
            }

            job2.join()

            delay(1000)

            launch {
                _showErrorToastChannel.send(false)
            }

        }
    }

    fun deleteRuta(
        kodeRuta: String
    ) {
        viewModelScope.launch {
            val job = launch {
                localRutaRepository.getRuta(kodeRuta).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            result.data?.let { response ->
                                _deleteRuta.value = response.toRuta()
                                Log.d(TAG, "getRuta succeed: $response")
                            }
                        }

                        is Result.Loading -> {
                            Log.d(TAG, "getRuta: Loading...")
                        }

                        is Result.Error -> {
                            result.message?.let { error ->
                                _errorMessage.value = error
                            }
                            _showErrorToastChannel.send(true)
                            Log.e(TAG, "getRuta: Error in getRuta")
                        }
                    }
                }
            }

            job.join() // Menunggu hingga coroutine di atas selesai

            launch {
                Log.d(TAG, "deleted ruta: ${_deleteRuta.value}")
                localRutaRepository.fakeDeleteRuta(_deleteRuta.value).collectLatest { message ->
                    Log.d(TAG, message)
                }
            }
        }
    }

    fun finalisasiBS(
        noBS: String
    ) {
        val wilayah = _wilayahWithAll.value.wilayahWithKeluarga!!.wilayah

        val updatedWilayah = wilayah!!.copy(status = "listing-selesai")

        viewModelScope.launch {
            val job = launch {
                remoteRutaRepository.finalisasiBS(noBS).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            result.data?.let { response ->
                                _finalisasiBSResponse.value = response
                                Log.d(
                                    TAG, "finalisasiBS succeed: $response"
                                )
                                _successMessage.value = "Berhasil melakukan finalisasi BS!"
                                _showSuccessToastChannel.send(true)
                            }
                        }

                        is Result.Loading -> {
                            Log.d(
                                TAG, "finalisasiBS: Loading..."
                            )
                        }

                        is Result.Error -> {
                            result.message?.let { error ->
                                _errorMessage.value = error
                            }
                            _showErrorToastChannel.send(true)
                            Log.e(
                                TAG, "finalisasiBS: Error in finalisasiBS"
                            )
                        }
                    }
                }
            }

            job.join()

            delay(2000)

            val job2 = launch {
                _finalisasiBSResponse.value.data?.forEach { ruta ->
                    localRutaRepository.updateRuta(ruta).collectLatest { message ->
                        Log.d(TAG, message)
                    }
                }
            }

            job2.join()

            val job3 = launch {
                showErrorToastChannel.collectLatest { isError ->
                    if (!isError) {
                        launch {
                            wilayahRepository.updateWilayah(
                                updatedWilayah.toWilayah(emptyList())
                            )
                                .collectLatest { message ->
                                    Log.d(TAG, message)
                                }
                        }
                    }
                }
            }

            job3.join()

            delay(1000)

            launch {
                _showErrorToastChannel.send(false)
            }
        }
    }

    fun sederhanakanNama(nama: String): String {
        val kata = nama.split(" ")
        return if (kata.size > 3) {
            val inisial = kata.subList(3, kata.size).joinToString(".") { it[0].toString() }
            kata.subList(0, 3).joinToString(" ") + " $inisial"
        } else {
            nama
        }
    }

}