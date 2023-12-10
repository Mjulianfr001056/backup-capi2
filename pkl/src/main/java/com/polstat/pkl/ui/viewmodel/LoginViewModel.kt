package com.polstat.pkl.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.polstat.pkl.configuration.RetrofitHelper
import com.polstat.pkl.response.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData<LoginResponse>()
    val loginState: LiveData<LoginResponse> = _loginState
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun login(nim: String, password: String) {
        uiScope.launch {
            try {
                val response = loginUser(nim, password)
                if (response.isSuccessful && response.body() != null) {
                    // Jika respons berhasil dan responseBody tidak null
                    _loginState.value = response.body()
                } else if (response.code() == 401) {
                    // Tangani kasus ketika kode respons adalah 401 (Unauthorized)
                    _loginState.value = LoginResponse("", "", "", "", "")
                } else {
                    // Tangani respons yang tidak berhasil di sini, misalnya:
                    _loginState.value =
                        LoginResponse("", "", "", "", "Gagal melakukan permintaan ke server")
                }
            } catch (e: Exception) {
                // Tangani kesalahan yang mungkin terjadi selama permintaan
                _loginState.value =
                    LoginResponse("", "", "", "", "Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private suspend fun loginUser(nim: String, password: String): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitHelper.apiService.loginUser(nim, password).execute()
//            RetrofitHelper.apiService.loginUser(LoginRequest(nim, password)).execute()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}