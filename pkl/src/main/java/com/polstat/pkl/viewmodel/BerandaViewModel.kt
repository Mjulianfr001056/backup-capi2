package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.relation.DataTimWithMahasiswa
import com.polstat.pkl.database.relation.MahasiswaWithWilayah
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.repository.DataTimRepository
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
class BerandaViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val dataTimRepository: DataTimRepository,
    private val mahasiswaRepository: MahasiswaRepository,
    private val wilayahRepository: WilayahRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_BERANDA_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session

    private val _dataTim = MutableStateFlow(DataTimEntity())

    val dataTim = _dataTim.asStateFlow()

    private val _dataTimWithMahasiswa = MutableStateFlow(DataTimWithMahasiswa())

    val dataTimWithMahasiswa = _dataTimWithMahasiswa.asStateFlow()

    private val _listMahasiswaWithWilayah = MutableStateFlow<List<MahasiswaWithWilayah>>(emptyList())

    val listMahasiswaWithWilayah = _listMahasiswaWithWilayah.asStateFlow()

    private val _listWilayahWithRuta = MutableStateFlow<List<WilayahWithRuta>>(emptyList())

    val listWilayahWithRuta = _listWilayahWithRuta.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {

        getMahasiswaWithWilayah(_session!!.nim)

        if (_session.isKoor) {

            getDataTimWithMahasiswa(_session.idTim)

            if (_dataTimWithMahasiswa.value.listMahasiswa.isNotEmpty()) {

                _dataTimWithMahasiswa.value.listMahasiswa.forEach { mahasiswa ->

                    getMahasiswaWithWilayah(mahasiswa.nim)

                    if (_listMahasiswaWithWilayah.value.isNotEmpty()) {

                        _listMahasiswaWithWilayah.value.forEach { mahasiswaWithWilayah ->

                            if (mahasiswaWithWilayah.listWilayah.isNotEmpty()) {

                                mahasiswaWithWilayah.listWilayah.forEach { wilayah ->

                                    getWilayahWithRuta(wilayah.noBS)

                                }
                            }
                        }
                    }
                }
            }

        } else {

            getDataTim(_session.idTim)

            if (_listMahasiswaWithWilayah.value.isNotEmpty()) {

                _listMahasiswaWithWilayah.value.forEach { mahasiswaWithWilayah ->

                    if (mahasiswaWithWilayah.listWilayah.isNotEmpty()) {

                        mahasiswaWithWilayah.listWilayah.forEach { wilayah ->

                            getWilayahWithRuta(wilayah.noBS)

                        }
                    }
                }
            }
        }

    }

    private fun getDataTim(
        idTim: String
    ) {
        viewModelScope.launch {
            dataTimRepository.getDataTim(idTim).collectLatest {result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _dataTim.value = response
                            Log.d(TAG, "getDataTim succeed: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getDataTim: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getDataTim: Error in getDataTim")
                    }
                }
            }
        }
    }

    private fun getDataTimWithMahasiswa(
        idTim: String
    ) {
        viewModelScope.launch {
            dataTimRepository.getDataTimWithMahasiswa(idTim).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            _dataTimWithMahasiswa.value = response
                            Log.d(TAG, "getDataTimWithMahasiswa succeed: $response")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getDataTimWithMahasiswa: Loading...")
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "getDataTimWithMahasiswa: Error in getDataTimWithMahasiswa")
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
                            _listMahasiswaWithWilayah.value += response
                            Log.d(TAG, "getMahasiswaWithWilayah succeed: $response")
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

    private fun getWilayahWithRuta(
        noBS: String
    ) {
       viewModelScope.launch {
           wilayahRepository.getWilayahWithRuta(noBS).collectLatest { result ->
               when(result) {
                   is Result.Success -> {
                       result.data?.let { response ->
                           _listWilayahWithRuta.value += response
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

}