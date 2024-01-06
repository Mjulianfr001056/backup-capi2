package com.polstat.pkl.auth.response

import com.polstat.pkl.main.domain.DataTim
import com.polstat.pkl.main.domain.Wilayah

data class AuthResponse(
    val avatar: String,
    val dataTim: DataTim,
    val id_kuesioner: String,
    val isKoor: Boolean,
    val nama: String,
    val nim: String,
    val status: String,
    val wilayah: Wilayah
)