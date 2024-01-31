package com.polstat.pkl.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.database.relation.DataTimWithAll
import com.polstat.pkl.database.relation.MahasiswaWithWilayah
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.repository.DataTimRepository
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.MahasiswaRepository
import com.polstat.pkl.repository.SampelRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    private val keluargaRepository: KeluargaRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val sampelRutaRepository: SampelRutaRepository,
    private val mahasiswaRepository: MahasiswaRepository,
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

    private val _mahasiswaWithWilayah = MutableStateFlow(MahasiswaWithWilayah())

    val mahasiswaWithWilayah = _mahasiswaWithWilayah.asStateFlow()

    private val _listSampelRuta = MutableStateFlow<List<SampelRutaEntity>>(listOf())

    val listSampelRuta = _listSampelRuta.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val getDataTimWithAllJob = async { getDataTimWithAll(_session?.idTim.toString()) }
            getDataTimWithAllJob.await()
            val getMahasiswaWithWilayahJob = async { getMahasiswaWithWilayah(_session?.nim.toString()) }
            getMahasiswaWithWilayahJob.await()
            _mahasiswaWithWilayah.value.listWilayah?.let {
                if (it.isNotEmpty()) {
                    getAllSampelRutaByNoBS(it[0].noBS)
                }
            }
        }
    }

    fun deleteAllLocalData() {
        viewModelScope.launch {

            dataTimRepository.deleteAllDataTim().collectLatest { message ->
                Log.d(TAG, "deleteAllLocalData: $message")
            }

            mahasiswaRepository.deleteAllMahasiswa().collectLatest { message ->
                Log.d(TAG, "deleteAllLocalData: $message")
            }

            wilayahRepository.deleteAllWilayah().collectLatest { message ->
                Log.d(TAG, "deleteAllLocalData: $message")
            }

            keluargaRepository.deleteAllKeluarga().collectLatest { message ->
                Log.d(TAG, "deleteAllLocalData: $message")
            }

            localRutaRepository.deleteAllRuta().collectLatest { message ->
                Log.d(TAG, "deleteAllLocalData: $message")
            }

            sampelRutaRepository.deleteAllSampelRuta().collectLatest { message ->
                Log.d(TAG, "deleteAllLocalData: $message")
            }

        }

    }

    fun logout(){
        sessionRepository.logOut()
        Log.d(TAG, "logout: Berhasil logout!")
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

    fun getAllSampelRutaByNoBS(
        noBS: String
    ) {
        viewModelScope.launch {
            sampelRutaRepository.getSampelRuta(noBS).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _listSampelRuta.value = response
                            Log.d(TAG, "getAllSampelRutaByNoBS succeed: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getAllSampelRutaByNoBS: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getAllSampelRutaByNoBS: Error in getAllSampelRutaByNoBS")
                    }
                }
            }
        }
    }

}