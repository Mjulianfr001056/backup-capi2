package com.polstat.pkl.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.polstat.pkl.ui.viewmodel.LoginViewModel

@Composable
fun SuccessScreen(loginViewModel: LoginViewModel) {
    val nama = loginViewModel.loginState.value?.nama
    println(loginViewModel.loginState.value)
    // Tampilan untuk screen sukses
    Text("Login berhasil, selamat datang $nama!")
}
