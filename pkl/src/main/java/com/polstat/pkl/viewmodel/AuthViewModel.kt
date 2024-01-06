package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.auth.repository.AuthRepository
import com.polstat.pkl.auth.response.AuthResponse
import com.polstat.pkl.main.domain.DataTim
import com.polstat.pkl.main.domain.Wilayah
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){

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

    private val _showLoadingToastChannel = Channel<Boolean>()
    val showLoadingToastChannel = _showLoadingToastChannel.receiveAsFlow()


    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    fun login(
        nim: String,
        password: String
    ){
        viewModelScope.launch {
            authRepository.login(nim, password).collectLatest { result ->
                when(result){
                    is Result.Success -> {
                        result.data?.let { response ->
                            _authResponse.value = response
                            Log.d("AuthViewModel", "Login successful: $response")
                        }
                    }

                    is Result.Loading -> {
                        _showLoadingToastChannel.send(true)
                        Log.d("AuthViewModel", "Loading...")
                    }

                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                        Log.e("AuthViewModel", "Error in login")
                    }
                }
            }
        }
    }
}
