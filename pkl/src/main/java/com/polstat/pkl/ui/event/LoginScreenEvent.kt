package com.polstat.pkl.ui.event

sealed class LoginScreenEvent {
    data class NimChanged(val nim: String) : LoginScreenEvent()
    data class PasswordChanged(val password: String) : LoginScreenEvent()

    object submit : LoginScreenEvent()
}