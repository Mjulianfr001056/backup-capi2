package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.repository.AuthRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.model.response.AuthResponse
import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.model.domain.User
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.utils.Result
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
): ViewModel(){

    companion object {
        private const val TAG = "SamplingViewModel"
    }

    val initialValue = AuthResponse(
        avatar = "",
        dataTim = DataTim(
            anggota = listOf(),
            idTim = "",
            namaTim = "",
            passPML = ""
        ),
        id_kuesioner = "",
        isKoor = false,
        nama = "",
        nim = "",
        status = "",
        wilayah = Wilayah(
            catatan = "",
            idKab = "",
            idKec = "",
            idKel = "",
            jmlGenZ = 0,
            jmlRt = 0,
            jmlRtGenz = 0,
            namaKab = "",
            namaKec = "",
            namaKel = "",
            noBS = "",
            ruta = listOf(),
            status = "",
            tglListing = Any(),
            tglPeriksa = Any()
        )
    )

    private val _authResponse = MutableStateFlow<AuthResponse>(initialValue)
    val authResponse = _authResponse.asStateFlow()

    private val _session = sessionRepository.getActiveSession()

    private val _showLoadingChannel = Channel<Boolean>()
    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    fun login(
        nim: String,
        password: String
    ){
        viewModelScope.launch {
            openLoadingDialog()
            authRepository.login(nim, password).collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        result.data?.let { response ->
                            _authResponse.value = response
                            Log.d(TAG, "Login successful: $response")
                        }
                    }

                    is Result.Loading -> {
                        Log.d(TAG, "Loading...")
                    }

                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "Error in login")
                    }
                }
            }
            delay(2000)
            closeLoadingDialog()
        }
    }

    private fun openLoadingDialog() {
        _showLoadingChannel.trySend(true)
    }

    private fun closeLoadingDialog() {
        _showLoadingChannel.trySend(false)
    }

    fun getUserFromSession() : User {
        return _session!!.user
    }

    fun getDataTimFromSession() : DataTim {
        return _session!!.dataTim
    }

    fun getWilayahFromSession() : Wilayah {
        return _session!!.wilayah
    }

}
