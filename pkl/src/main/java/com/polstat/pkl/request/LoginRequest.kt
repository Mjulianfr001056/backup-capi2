package com.polstat.pkl.request

/**
 * @author Julian Firdaus
 * @since 09/12/2023
 *
 */
data class LoginRequest(
    val nim: String,
    val password: String
)