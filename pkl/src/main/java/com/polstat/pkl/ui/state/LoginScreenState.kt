package com.polstat.pkl.ui.state

data class LoginScreenState(
    val nim: String = "",
    val nimError: String? = null,
    val password: String = "",
    val passwordError: String? = null
)