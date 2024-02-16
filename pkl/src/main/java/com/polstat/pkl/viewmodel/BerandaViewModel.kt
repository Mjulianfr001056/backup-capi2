package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.repository.AnggotaTimRepository
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.LocationRepository
import com.polstat.pkl.repository.SampelRutaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.utils.Result
import com.polstat.pkl.utils.location.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BerandaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val wilayahRepository: WilayahRepository,
    private val sampelRutaRepository: SampelRutaRepository,
    private val anggotaTimRepository: AnggotaTimRepository,
    private val keluargaRepository: KeluargaRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val getLocationUseCase: GetLocationUseCase,
    private val locationRepository: LocationRepository
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

    private val _isAskLocation = MutableStateFlow(false)

    val askLocation = _isAskLocation.asStateFlow()

    init {
        viewModelScope.launch {
//            delay(10000L)
            getLocationUseCase.invoke().collect { location ->
                if (location == null) {
                    _isAskLocation.value = true
                }
                if (location != null && session?.nim != null) {
                    Log.d(TAG, "getLocationUseCase: Nim = ${session.nim}")
                    locationRepository.updateLocation(
                        session.nim,
                        location.longitude,
                        location.latitude,
                        location.accuracy
                    )
                }
                Log.d(TAG, "getCurrentLocation: ${location?.latitude}, ${location?.longitude}, ${location?.accuracy}")
            }
        }
        getAllWilayah()
        getAllAnggotaTim()
        getAllSampelRuta()
    }

    private fun getAllWilayah() {
        viewModelScope.launch {
            wilayahRepository.getAllWilayah().collectLatest { result ->
                when (result) {
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

    private fun getAllAnggotaTim() {
        viewModelScope.launch {
            anggotaTimRepository.getAllAnggotaTim().collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                            Log.e(TAG, "getAllAnggotaTim: Error in getAllAnggotaTim ($errorMessage)")
                        }
                        _showErrorToastChannel.send(true)
                    }
                    is Result.Loading -> Log.d(TAG, "getAllAnggotaTim: Loading...")
                    is Result.Success -> {
                        result.data?.let {
                            _listAnggotaTim.value = it
                            Log.d(TAG, "getAllAnggotaTim: $listAnggotaTim")
                        }
                    }
                }
            }
        }
    }

    private fun getAllSampelRuta() {
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

    fun deleteAllLocalData() {
        viewModelScope.launch {
            anggotaTimRepository.deleteAllAnggotaTim().collectLatest { message ->
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
}