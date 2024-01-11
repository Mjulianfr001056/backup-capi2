package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.model.domain.Mahasiswa
import com.polstat.pkl.model.response.AuthResponse
import com.polstat.pkl.repository.AuthRepository
import com.polstat.pkl.repository.DataTimRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.MahasiswaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.ui.event.LoginScreenEvent
import com.polstat.pkl.ui.state.LoginScreenState
import com.polstat.pkl.utils.Result
import com.polstat.pkl.utils.use_case.ValidateNim
import com.polstat.pkl.utils.use_case.ValidatePassword
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
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val dataTimRepository: DataTimRepository,
    private val mahasiswaRepository: MahasiswaRepository,
    private val wilayahRepository: WilayahRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val validateNim: ValidateNim,
    private val validatePassword: ValidatePassword
): ViewModel(){

    var state by mutableStateOf(LoginScreenState())

    companion object {
        private const val TAG = "CAPI63_AUTH_VM"
    }

    private val initialValue = AuthResponse()

    private val _authResponse = MutableStateFlow(initialValue)

    val authResponse = _authResponse.asStateFlow()

    private val _errorMessage = MutableStateFlow("")

    val errorMessage = _errorMessage.asStateFlow()

    private val _session = sessionRepository.getActiveSession()

    private val _showLoadingChannel = Channel<Boolean>()

    val showLoadingChannel = _showLoadingChannel.receiveAsFlow()

    private val _showErrorToastChannel = Channel<Boolean>()

    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    @Suppress("NAME_SHADOWING")
    fun login(
        nim: String,
        password: String
    ){
        viewModelScope.launch {
            openLoadingDialog()
            val loginDeferred = async {
                authRepository.login(nim, password).collectLatest { result ->
                    when (result) {
                        is Result.Success -> {
                            result.data?.let { response ->
                                Log.d("AVM", "${response}")
                                val mahasiswa = Mahasiswa(
                                    alamat = "",
                                    email = "",
                                    foto = response.avatar,
                                    id_tim = response.dataTim.idTim,
                                    isKoor = response.isKoor,
                                    nama = response.nama,
                                    nim = response.nim,
                                    no_hp = "",
                                    password = "",
                                    wilayah_kerja = emptyList()
                                )
                                mahasiswaRepository.insertMahasiswa(mahasiswa)

                                if (response.dataTim.idTim != "") {

                                    val dataTim = if (response.isKoor) DataTim(
                                        idTim = response.dataTim.idTim,
                                        namaTim = response.dataTim.namaTim,
                                        passPML = response.dataTim.passPML,
                                        namaPML = response.nama,
                                        nimPML = response.nim,
                                        teleponPML = "",
                                        anggota = response.dataTim.anggota
                                    ) else response.dataTim

                                    dataTimRepository.insertDataTim(dataTim)

                                    if (response.dataTim.anggota.isNotEmpty()) {
                                        response.dataTim.anggota.forEach { mahasiswa ->
                                            mahasiswaRepository.insertMahasiswa(mahasiswa)

                                            if (mahasiswa.wilayah_kerja.isNotEmpty()) {
                                                mahasiswa.wilayah_kerja.forEach { wilayah ->
                                                    wilayahRepository.insertWilayah(
                                                        wilayah,
                                                        mahasiswa.nim
                                                    )

                                                    if (wilayah.ruta.isNotEmpty()) {
                                                        wilayah.ruta.forEach { ruta ->
                                                            localRutaRepository.insertRuta(ruta)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (response.wilayah.isNotEmpty()) {
                                    response.wilayah.forEach { wilayah ->
                                        wilayahRepository.insertWilayah(wilayah, response.nim)

                                        if (wilayah.ruta.isNotEmpty()) {
                                            wilayah.ruta.forEach { ruta ->
                                                localRutaRepository.insertRuta(ruta)
                                            }
                                        }
                                    }
                                }
                                _authResponse.value = response
                                Log.d(TAG, "Login successful: $response")
                            }
                        }

                        is Result.Loading -> {
                            Log.d(TAG, "Loading...")
                        }

                        is Result.Error -> {
                            result.message?.let { error ->
                                _errorMessage.value = error
                            }
                            _showErrorToastChannel.send(true)
                            Log.e(TAG, "Error in login")
                        }
                    }
                }
            }
//            delay(2000)
            loginDeferred.await()
            closeLoadingDialog()
        }
    }

    fun onEvent(event: LoginScreenEvent){
        when(event){
            is LoginScreenEvent.NimChanged -> {
                state = state.copy(nim = event.nim)
            }
            is LoginScreenEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is LoginScreenEvent.submit -> {
                validateForm()
            }
        }
    }

    private fun validateForm(){
        val nimResult = validateNim.execute(state.nim)
        val passwordResult = validatePassword.execute(state.password)

        val hasError = listOf(nimResult, passwordResult)
            .any { !it.successful }

        if(hasError){
            state = state.copy(
                nimError = nimResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
        } else {
            login(state.nim, state.password)
            return
        }
    }

    private fun openLoadingDialog() {
        _showLoadingChannel.trySend(true)
    }

    private fun closeLoadingDialog() {
        _showLoadingChannel.trySend(false)
    }

//    fun getUserFromSession() : Session {
//        return _session!!.user
//    }
//
//    fun getDataTimFromSession() : DataTim {
//        return _session!!.dataTim
//    }
//
//    fun getWilayahFromSession() : List<Wilayah> {
//        return _session!!.wilayah
//    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}
