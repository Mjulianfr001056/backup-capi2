package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.mapper.toKeluargaDto
import com.polstat.pkl.mapper.toRuta
import com.polstat.pkl.mapper.toRutaDtoList
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.request.JsonKlg
import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.FinalisasiBSResponse
import com.polstat.pkl.model.response.SyncRutaResponse
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.RemoteRutaRepository
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
class ListRutaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val keluargaRepository: KeluargaRepository,
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

    val idBS = savedStateHandle.get<String>("idBS")

    private val _listKeluargaByWilayah = MutableStateFlow<List<KeluargaEntity>>(emptyList())

    val listKeluargaByWilayah = _listKeluargaByWilayah.asStateFlow()

    private val _listRutaByWilayah = MutableStateFlow<List<RutaEntity>>(emptyList())

    val listRutaByWilayah = _listRutaByWilayah.asStateFlow()

    private val _listKeluargaByRuta = MutableStateFlow<List<KeluargaEntity>>(emptyList())

    val listKeluargaByRuta = _listKeluargaByRuta.asStateFlow()

    private val _listRutaByKeluarga = MutableStateFlow<List<RutaEntity>>(emptyList())

    val listRutaByKeluarga = _listRutaByKeluarga.asStateFlow()

    private val _synchronizeRuta = MutableStateFlow(SyncRutaResponse())

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
        idBS?.let { getAllRutaByWilayah(it) }
    }

    fun getAllRutaByWilayah(idBS: String) {
        viewModelScope.launch {
            localRutaRepository.getAllRutaByWilayah(idBS).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getAllRutaByWilayah: Error in getAllRutaByWilayah ($errorMessage)")
                        }
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Loading -> Log.d(TAG, "getAllRutaByWilayah: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listRutaByWilayah.value = it
                            _successMessage.value = "Berhasil mendapatkan semua rumah tangga!"
                            _showSuccessToastChannel.send(true)
                            Log.d(TAG, "getAllRutaByWilayah succeed: $listRutaByWilayah")
                        }
                    }
                }
            }
        }
    }

    fun getAllKeluargaByWilayah(idBS: String) {
        viewModelScope.launch {
            keluargaRepository.getAllKeluargaByWilayah(idBS).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getAllKeluargaByWilayah: Error in getAllKeluargaByWilayah ($errorMessage)")
                        }
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Loading -> Log.d(TAG, "getAllKeluargaByWilayah: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listKeluargaByWilayah.value = it
                            _successMessage.value = "Berhasil mendapatkan semua keluarga!"
                            _showSuccessToastChannel.send(true)
                            Log.d(TAG, "getAllKeluargaByWilayah succeed: $listKeluargaByWilayah")
                        }
                    }
                }
            }
        }
    }

    fun getAllRutaByKeluarga(kodeKlg: String) {
        viewModelScope.launch {
            localRutaRepository.getAllRutaByKeluarga(kodeKlg).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getAllRutaByKeluarga: Error in getAllRutaByKeluarga ($errorMessage)")
                        }
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Loading -> Log.d(TAG, "getAllRutaByKeluarga: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listRutaByKeluarga.value = it
                            Log.d(TAG, "getAllRutaByKeluarga: Berhasil mendapat seluruh ruta by keluarga! $listRutaByKeluarga")
                        }
                    }
                }
            }
        }
    }

    fun getAllKeluargaByRuta(kodeRuta: String) {
        viewModelScope.launch {
            keluargaRepository.getAllKeluargaByRuta(kodeRuta).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getAllKeluargaByRuta: Error in getAllKeluargaByRuta ($errorMessage)")
                        }
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Loading -> Log.d(TAG, "getAllKeluargaByRuta: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listKeluargaByRuta.value = it
                            Log.d(TAG, "getAllKeluargaByRuta: Berhasil mendapat seluruh keluarga by ruta! $listKeluargaByRuta")
                        }
                    }
                }
            }
        }
    }

    fun synchronizeRuta(
        listKeluargaByWilayah: List<KeluargaEntity>,
        nim: String,
        idBS: String
    ) {
        val jsonKlgInstance = JsonKlg()

        listKeluargaByWilayah.forEach { keluargaEntity ->
            getAllRutaByKeluarga(keluargaEntity.kodeKlg)
            val listFilteredRuta = listRutaByKeluarga.value.filter { it.status != "fetch" }
            jsonKlgInstance.add(keluargaEntity.toKeluargaDto(listFilteredRuta.toRutaDtoList()))
        }

        val syncRutaRequest = SyncRutaRequest(
            nim = nim,
            id_bs = idBS,
            json = jsonKlgInstance
        )
        Log.d(TAG, "synchronizeRuta: SyncRutaRequest $syncRutaRequest")
        viewModelScope.launch {
            remoteRutaRepository.sinkronisasiRuta(syncRutaRequest).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _synchronizeRuta.value = response
                            _successMessage.value = "Berhasil melakukan sinkronisasi!"
                            _showSuccessToastChannel.send(true)
                            Log.d(
                                TAG, "synchronizeRuta succeed: $_synchronizeRuta"
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
                            Log.e(
                                TAG,
                                "synchronizeRuta: Error in synchronizeRuta ($errorMessage)"
                            )
                        }
                        _showErrorToastChannel.send(true)
                    }
                }
            }
        }
    }

    fun generateRuta(idBS: String) {
        viewModelScope.launch {
            remoteRutaRepository.generateRuta(idBS).collectLatest { message ->
                _successMessage.value = message
                _showSuccessToastChannel.send(true)
                Log.d(TAG, message)
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
        idBS: String
    ) {
        viewModelScope.launch {
            remoteRutaRepository.finalisasiBS(idBS).collectLatest { result ->
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