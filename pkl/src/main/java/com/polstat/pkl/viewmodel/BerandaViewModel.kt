package com.polstat.pkl.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.database.entity.WilayahEntity
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

    val session = sessionRepository.getActiveSession()

    private val _listWilayah = MutableStateFlow<List<WilayahEntity>>(emptyList())

    val listWilayah = _listWilayah.asStateFlow()

    private val _listAllSampelRuta = MutableStateFlow<List<SampelRutaEntity>>(emptyList())

    val listAllSampelRuta = _listAllSampelRuta.asStateFlow()

    private val _listAnggotaTim = MutableStateFlow<List<AnggotaTimEntity>>(emptyList())

    val listAnggotaTim = _listAnggotaTim.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {

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

    fun getAllSampelRuta() {
        viewModelScope.launch {
            sampelRutaRepository.getAllSampelRuta().collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _listAllSampelRuta.value = response
                            Log.d(TAG, "getAllSampelRuta succeed: $listAllSampelRuta")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getAllSampelRutaByNoBS: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getAllSampelRuta: Error in getAllSampelRuta ($errorMessage)")
                        }
                        _showErrorToastChannel.send(true)
                    }
                }
            }
        }
    }
}