package com.polstat.pkl.auth.repository

data class Session(
    val nama: String,
    val nim: String,
    val avatar: String,
    val isKoor: Boolean,
    val id_kuesioner: String
)
