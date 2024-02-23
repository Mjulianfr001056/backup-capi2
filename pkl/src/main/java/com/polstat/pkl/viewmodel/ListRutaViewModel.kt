package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.database.relation.RutaWithKeluarga
import com.polstat.pkl.mapper.toKeluarga
import com.polstat.pkl.mapper.toKeluargaDto
import com.polstat.pkl.mapper.toRuta
import com.polstat.pkl.mapper.toRutaDtoList
import com.polstat.pkl.mapper.toWilayah
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.request.JsonKlg
import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.FinalisasiBSResponse
import com.polstat.pkl.model.response.SyncRutaResponse
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.RemoteRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    private val wilayahRepository: WilayahRepository,
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

    val idBS = savedStateHandle.get<String>("idBS")

    val isMonitoring = savedStateHandle.get<Boolean>("isMonitoring")

    val isListRuta = savedStateHandle.get<Boolean>("isListRuta")

    private val _wilayah = MutableStateFlow(WilayahEntity())

    val wilayah = _wilayah.asStateFlow()

    private val _listKeluargaWithRuta = MutableStateFlow<List<KeluargaWithRuta>>(emptyList())

    val listKeluargaWithRuta = _listKeluargaWithRuta.asStateFlow()

    private val _listRutaWithKeluarga = MutableStateFlow<List<RutaWithKeluarga>>(emptyList())

    val listRutaWithKeluarga = _listRutaWithKeluarga.asStateFlow()

    private val _listKeluargaByRuta = MutableStateFlow<List<KeluargaEntity>>(emptyList())

    private val _synchronizeRuta = MutableStateFlow(SyncRutaResponse())

    val synchronizeRuta = _synchronizeRuta.asStateFlow()

    private val _deleteRuta = MutableStateFlow(RutaEntity())

    private val _deleteKeluarga = MutableStateFlow(KeluargaEntity())

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

    private val _isSuccesed = MutableStateFlow(false)

    val isSuccesed = _isSuccesed.asStateFlow()

    init {
        viewModelScope.launch {
            idBS?.let {
                getWilayah(it)
                getListRutaWithKeluarga(it)
                getListKeluargaWithRuta(it)
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
                        }
                        _showErrorToastChannel.send(true)
                        _isSuccesed.value = true
                    }
                    is Result.Loading -> Log.d(TAG, "getWilayah: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _wilayah.value = it
                            Log.d(TAG, "getWilayah succeed: ${wilayah.value}")
                        }
                    }
                }
            }
        }
    }

    suspend fun getListRutaWithKeluarga(idBS: String) {
        viewModelScope.launch (Dispatchers.IO) {
            localRutaRepository.getListRutaWithKeluarga(idBS).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getListRutaWithKeluarga: Error in getListRutaWithKeluarga (${errorMessage.value})")
                        }
                        _showErrorToastChannel.send(true)
                        _isSuccesed.value = true
                    }
                    is Result.Loading -> Log.d(TAG, "getListRutaWithKeluarga: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listRutaWithKeluarga.value = it
                            _successMessage.value = "Data berhasil ditampilkan"
                            Log.d(TAG, "getListRutaWithKeluarga succeed: ${listRutaWithKeluarga.value}")
                        }
                        _showSuccessToastChannel.send(true)
                    }
                }
                if (_listRutaWithKeluarga.value.isNullOrEmpty()){
                    _successMessage.value = "Data tidak ditemukan"
                    _showSuccessToastChannel.send(true)
                }

                if (!_listRutaWithKeluarga.value.isNullOrEmpty()){
                    _successMessage.value = "Data berhasil ditampilkan"
                    _showSuccessToastChannel.send(true)
                }
            }
        }
    }

    suspend fun getListKeluargaWithRuta(idBS: String) {
        viewModelScope.launch (Dispatchers.IO) {
            keluargaRepository.getListKeluargaWithRuta(idBS).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getListKeluargaWithRuta: Error in getListKeluargaWithRuta (${errorMessage.value})")
                        }
                        _showErrorToastChannel.send(true)
                        _isSuccesed.value = true
                    }
                    is Result.Loading -> Log.d(TAG, "getListKeluargaWithRuta: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listKeluargaWithRuta.value = it
                            _successMessage.value = "Data berhasil ditampilkan"
                            Log.d(TAG, "getListKeluargaWithRuta succeed: ${listKeluargaWithRuta.value}")
                        }
                        _showSuccessToastChannel.send(true)
                    }
                }
                if (_listKeluargaWithRuta.value.isNullOrEmpty()){
                    _successMessage.value = "Data tidak ditemukan"
                    _showSuccessToastChannel.send(true)
                }

                if (!_listKeluargaWithRuta.value.isNullOrEmpty()){
                    _successMessage.value = "Data berhasil ditampilkan"
                    _showSuccessToastChannel.send(true)
                }
            }
        }
    }

    fun synchronizeRuta(
        listKeluargaWithRuta: List<KeluargaWithRuta>,
        nim: String,
        idBS: String
    ) {
        val jsonKlgInstance = JsonKlg()

        listKeluargaWithRuta.filter { it.keluarga.status != "fetch" }.forEach { keluargaWithRuta ->
            val listFilteredRuta = keluargaWithRuta.listRuta.filter { it.status != "fetch" }
            jsonKlgInstance.add(keluargaWithRuta.keluarga.toKeluargaDto(listFilteredRuta.toRutaDtoList()))
        }

        val syncRutaRequest = SyncRutaRequest(
            nim = nim,
            id_bs = idBS,
            json = jsonKlgInstance
        )
        Log.d(TAG, "synchronizeRuta: SyncRutaRequest $syncRutaRequest")
        viewModelScope.launch {
            val synchronizeJob = async {
                remoteRutaRepository.sinkronisasiRuta(syncRutaRequest).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            result.data?.let { response ->
                                _synchronizeRuta.value = response
                                _successMessage.value = "Berhasil melakukan sinkronisasi!"
                                _showSuccessToastChannel.send(true)
                                Log.d(TAG, "synchronizeRuta succeed: ${_synchronizeRuta.value}")
                            }
                        }

                        is Result.Loading -> Log.d(TAG, "synchronizeRuta: Loading...")

                        is Result.Error -> {
                            result.message?.let { error ->
                                _errorMessage.value = error
                                Log.e(TAG, "synchronizeRuta: Error in synchronizeRuta (${errorMessage.value})")
                            }
                            _showErrorToastChannel.send(true)
                            _isSuccesed.value = true
                        }
                    }
                }
            }
            synchronizeJob.await()

            if (successMessage.value == "Berhasil melakukan sinkronisasi!") {
                //Hapus semua data keluarga dan ruta di lokal
                val deleteAllKeluargaRutaAndRelationByWilayahJob = async {
                    localRutaRepository.deleteAllKeluargaRutaAndRelationByWilayah(idBS).collectLatest { message ->
                        Log.d(TAG, message)
                    }
                }
                deleteAllKeluargaRutaAndRelationByWilayahJob.await()

                //Update data wilayah terbaru
                wilayahRepository.insertWilayah(synchronizeRuta.value.toWilayah()).collectLatest { message ->
                    Log.d(TAG, message)
                }

                synchronizeRuta.value.keluarga.forEach { klg ->
                    //Update data keluarga terbaru
                    keluargaRepository.insertKeluarga(klg, KeluargaRepository.Method.Fetch).collectLatest { message ->
                        Log.d(TAG, message)
                    }
                    klg.ruta.forEach { ruta ->
                        //Update data ruta terbaru
                        localRutaRepository.insertRuta(ruta, LocalRutaRepository.Method.Fetch).collectLatest { message ->
                            Log.d(TAG, message)
                        }

                        localRutaRepository.insertKeluargaAndRuta(klg.kodeKlg, ruta.kodeRuta).collectLatest { message ->
                            Log.d(TAG, message)
                        }
                    }
                }
                _isSuccesed.value = true
            }
        }
    }

    suspend fun deleteRuta(
        idBS: String,
        noSegmen: String,
        kodeRuta: String
    ) {
        viewModelScope.launch {
            val job = launch {
                localRutaRepository.getRuta(idBS, noSegmen, kodeRuta).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            result.data?.let { response ->
                                _deleteRuta.value = response
                                Log.d(TAG, "getRuta succeed: $response")
                            }
                        }

                        is Result.Loading -> Log.d(TAG, "getRuta: Loading...")

                        is Result.Error -> {
                            result.message?.let { error ->
                                _errorMessage.value = error
                                Log.e(TAG, "getRuta: Error in getRuta (${errorMessage.value}")
                            }
                            _showErrorToastChannel.send(true)
                            _isSuccesed.value = true
                        }
                    }
                }
            }

            job.join() // Menunggu hingga coroutine di atas selesai

            val job2 = launch {
                Log.d(TAG, "deleted ruta: ${_deleteRuta.value.toRuta()}")
                localRutaRepository.fakeDeleteRuta(_deleteRuta.value.toRuta()).collectLatest { message ->
                    Log.d(TAG, message)
                }
            }
            job2.join()

            val job3 = launch {
                keluargaRepository.getAllKeluargaByRuta(_deleteRuta.value.kodeRuta).collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            result.message?.let { error ->
                                _errorMessage.value = error
                                Log.e(TAG, "getAllKeluargaByRuta: Error in getAllKeluargaByRuta (${errorMessage.value}")
                            }
//                            _showErrorToastChannel.send(true)
                        }

                        is Result.Loading ->  Log.d(TAG, "getRuta: Loading...")

                        is Result.Success -> {
                            result.data?.let {
                                _listKeluargaByRuta.value = it
                                Log.d(TAG, "getAllKeluargaByRuta succeed: ${_listKeluargaByRuta.value}")
                            }
                        }
                    }
                }
            }
            job3.join()

            _listKeluargaByRuta.value.forEach { keluargaEntity ->
                val updatedKlg = keluargaEntity.copy(status = "update")
                Log.d(TAG, "deleteRuta: updatedKlg ${updatedKlg.toKeluarga()}")
                keluargaRepository.updateKeluarga(updatedKlg.toKeluarga()).collectLatest { message ->
                    Log.d(TAG, message)
                }
            }

            _isSuccesed.value = true
        }
    }

    suspend fun deleteKeluarga(
        idBS: String,
        noSegmen: String,
        kodeKlg: String
    ) {
        viewModelScope.launch {
            val getKeluargaJob = async {
                keluargaRepository.getKeluarga(idBS, noSegmen, kodeKlg).collectLatest { result ->
                    when(result) {
                        is Result.Error -> {
                            result.message?.let { error ->
                                _errorMessage.value = error
                            }
                            _isSuccesed.value = true
                        }
                        is Result.Loading -> Log.d(TAG, "getKeluarga: Loading...")
                        is Result.Success -> {
                            result.data?.let {
                                _deleteKeluarga.value = it
                                Log.d(TAG, "deleteKeluarga: Berhasil mendapatkan keluarga! ${_deleteKeluarga.value}")
                            }
                        }
                    }
                }
            }
            getKeluargaJob.await()

            Log.d(TAG, "deleteKeluarga: fakeDeleteKeluarga ${_deleteKeluarga.value.toKeluarga()}")
            keluargaRepository.fakeDeleteKeluarga(_deleteKeluarga.value.toKeluarga()).collectLatest { message ->
                Log.d(TAG, message)
            }
            _isSuccesed.value = true
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

    // Fungsi untuk mendapatkan semua kodeKlg
    fun getAllKodeKlgFromRutaWithKeluarga(rutaWithKeluarga: RutaWithKeluarga): List<String> {
        return rutaWithKeluarga.listKeluarga.map { it.kodeKlg }
    }

    fun updateShowLoading(isSuccesed: Boolean) {
        _showLoadingChannel.trySend(!isSuccesed)
    }
}