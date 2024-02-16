package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.model.domain.AnggotaTim
import com.polstat.pkl.model.response.AuthResponse
import com.polstat.pkl.repository.AnggotaTimRepository
import com.polstat.pkl.repository.AuthRepository
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.LocationRepository
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val anggotaTimRepository: AnggotaTimRepository,
    private val wilayahRepository: WilayahRepository,
    private val keluargaRepository: KeluargaRepository,
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

    val visiblePermissionDialogQueue = mutableListOf<String>()

    init {

    }

    fun isLoggedIn() : Boolean {
        return sessionRepository.isLoggedIn()
    }

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
                            _authResponse.value = response
                            Log.d(TAG, "Login successful")
                        }
                        state = state.copy(nim = "")
                        state = state.copy(password = "")
                    }

                    is Result.Loading -> {
                        Log.d(TAG, "Loading...")
                    }

                    is Result.Error -> {
                        result.message?.let { error ->
                            _errorMessage.value = error
                        }
                        _showErrorToastChannel.send(true)
                        Log.e(TAG, "Error in login: $errorMessage")
                        Log.d(TAG, "login: ${authResponse.value}")
                    }
                }
            }

            if (authResponse.value.nim.isEmpty()) {
                delay(2000)
                Log.d(TAG, "login: nim empty")
                closeLoadingDialog()
                Log.d(TAG, "login: error $showErrorToastChannel, loading $showLoadingChannel")
                return@launch
            }

            saveDataTim(authResponse.value)

            //Redundant but preferred
            if (authResponse.value.wilayah.isEmpty()) {
                return@launch
            }

            authResponse.value.wilayah.forEach { wilayah ->
                wilayahRepository.insertWilayah(wilayah)
                    .collectLatest { message ->
                        Log.d(TAG, message)
                    }

                wilayah.keluarga.forEach { keluarga ->
                    keluargaRepository.insertKeluarga(keluarga, KeluargaRepository.Method.Fetch)
                        .collectLatest { message ->
                            Log.d(TAG, message)
                        }

                    keluarga.ruta.forEach { ruta ->
                        localRutaRepository.insertRuta(ruta, LocalRutaRepository.Method.Fetch)
                            .collectLatest { message ->
                                Log.d(TAG, message)
                            }
                        localRutaRepository.insertKeluargaAndRuta(keluarga.kodeKlg, ruta.kodeRuta)
                            .collectLatest { message ->
                                Log.d(TAG, message)
                            }
                    }
                }
            }

            Log.d(TAG, "Token: ${_session?.token} ")
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

    private suspend fun saveDataTim(user: AuthResponse){
        //Cek apakah user adalah koor atau tidak dan apakah memiliki anggota tim atau tidak
        if (!user.isKoor || user.dataTim.anggota.isEmpty()) {
            return
        }

        user.dataTim.anggota.forEach {
            val anggotaTim = AnggotaTim(
                nim = it.nim,
                nama = it.nama,
                noTlp = it.no_hp
            )
            anggotaTimRepository.insertAnggotaTim(anggotaTim)
                .collectLatest { message ->
                    Log.d(TAG, message)
                }
        }
    }

    fun dismissPermissionDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}
