package com.polstat.pkl.response

//data class LoginResponse(val success: Boolean = false, val token: String, val error: String?, val nama: String)
data class LoginResponse(
    val nim: String,
    val nama: String,
    val no_hp: String?,
    val alamat: String,
    val password: String
)
