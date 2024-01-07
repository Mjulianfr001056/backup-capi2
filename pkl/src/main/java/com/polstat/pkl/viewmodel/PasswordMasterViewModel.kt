package com.polstat.pkl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PasswordMasterViewModel : ViewModel() {
    private val _randomString = MutableStateFlow(generateRandomString())
    val randomString: StateFlow<String> = _randomString.asStateFlow()

    private val _countdown = MutableStateFlow(100)  // Ubah angka ini untuk mengatur waktu countdown
    val countdown: StateFlow<Int> = _countdown.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)  // Ubah waktu delay di sini (dalam milidetik)
                if (_countdown.value > 0) {
                    _countdown.value--
                } else {
                    _randomString.value = generateRandomString()
                    _countdown.value = 100  // Reset countdown
                }
            }
        }
    }

    fun generateRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..7)
            .map { allowedChars.random() }
            .joinToString("")
    }

}