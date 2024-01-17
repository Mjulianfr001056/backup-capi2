package com.polstat.pkl.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.mapper.toMahasiswaEntity
import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.model.domain.Mahasiswa
import com.polstat.pkl.model.response.AuthResponse
import com.polstat.pkl.repository.AuthRepository
import com.polstat.pkl.repository.DataTimRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.LocationRepository
import com.polstat.pkl.repository.MahasiswaRepository
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.ui.event.LoginScreenEvent
import com.polstat.pkl.ui.state.LoginScreenState
import com.polstat.pkl.utils.Result
import com.polstat.pkl.utils.location.GetLocationUseCase
import com.polstat.pkl.utils.use_case.ValidateNim
import com.polstat.pkl.utils.use_case.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val dataTimRepository: DataTimRepository,
    private val mahasiswaRepository: MahasiswaRepository,
    private val wilayahRepository: WilayahRepository,
    private val localRutaRepository: LocalRutaRepository,
    private val validateNim: ValidateNim,
    private val validatePassword: ValidatePassword,
    private val getLocationUseCase: GetLocationUseCase,
    private val locationRepository: LocationRepository
) : ViewModel() {

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

    private val _countdown = MutableStateFlow(10)

    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_countdown.value > 0) {
                    _countdown.value--
                } else {
                    getLocationUseCase.invoke().collect { location ->
                        if (location != null && _session?.nim != null) {
                            locationRepository.updateLocation(_session.nim,location.longitude,location.latitude,location.accuracy)
                        }
                        Log.d(TAG, "getLocationUseCase: ${location!!.latitude}, ${location.longitude}, ${location.accuracy}")
                    }
                    _countdown.value = 10
                }
            }
        }
    }

    @Suppress("NAME_SHADOWING")
    fun login(
        nim: String,
        password: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            openLoadingDialog()
            authRepository.login(nim, password).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        result.data?.let { response ->
                            Log.d(TAG, "$response")

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

            val mahasiswa = Mahasiswa(
                alamat = "",
                email = "",
                foto = authResponse.value.avatar,
                id_tim = authResponse.value.dataTim.idTim,
                isKoor = authResponse.value.isKoor,
                nama = authResponse.value.nama,
                nim = authResponse.value.nim,
                no_hp = "",
                password = "",
                wilayah_kerja = emptyList()
            )
            mahasiswaRepository.insertMahasiswa(mahasiswa)
                .collectLatest { message ->
                    Log.d(TAG, "${message}: ${mahasiswa.toMahasiswaEntity()}")
                }

            if (authResponse.value.dataTim.idTim != "") {

                val dataTim = if (authResponse.value.isKoor) DataTim(
                    idTim = authResponse.value.dataTim.idTim,
                    namaTim = authResponse.value.dataTim.namaTim,
                    passPML = authResponse.value.dataTim.passPML,
                    namaPML = authResponse.value.nama,
                    nimPML = authResponse.value.nim,
                    teleponPML = "",
                    anggota = authResponse.value.dataTim.anggota
                ) else authResponse.value.dataTim

                dataTimRepository.insertDataTim(dataTim).collectLatest { message ->
                    Log.d(TAG, message)
                }

                if (authResponse.value.dataTim.anggota!!.isNotEmpty()) {
                    authResponse.value.dataTim.anggota!!.forEach { mahasiswa ->
                        mahasiswaRepository.insertMahasiswa(mahasiswa)
                            .collectLatest { message ->
                                Log.d(TAG, message)
                            }

                        if (mahasiswa.wilayah_kerja?.isNotEmpty() == true) {
                            mahasiswa.wilayah_kerja.forEach { wilayah ->
                                wilayahRepository.insertWilayah(
                                    wilayah,
                                    mahasiswa.nim
                                ).collectLatest { message ->
                                    Log.d(TAG, message)
                                }

                                if (wilayah.ruta!!.isNotEmpty()) {
                                    wilayah.ruta.forEach { ruta ->
                                        localRutaRepository.insertRuta(ruta)
                                            .collectLatest { message ->
                                                Log.d(TAG, message)
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (authResponse.value.wilayah.isNotEmpty()) {
                authResponse.value.wilayah.forEach { wilayah ->
                    wilayahRepository.insertWilayah(wilayah, authResponse.value.nim)
                        .collectLatest { message ->
                            Log.d(TAG, message)
                        }

                    if (wilayah.ruta!!.isNotEmpty()) {
                        wilayah.ruta.forEach { ruta ->
                            localRutaRepository.insertRuta(ruta)
                                .collectLatest { message ->
                                    Log.d(TAG, message)
                                }
                        }
                    }
                }
            }

            delay(2000)
            closeLoadingDialog()
        }
    }

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
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

    private fun validateForm() {
        val nimResult = validateNim.execute(state.nim)
        val passwordResult = validatePassword.execute(state.password)

        val hasError = listOf(nimResult, passwordResult)
            .any { !it.successful }

        if (hasError) {
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
