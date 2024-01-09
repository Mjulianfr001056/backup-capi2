package com.polstat.pkl.model.response

import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.model.domain.Wilayah

data class AuthResponse(
    val avatar: String = "",
    val dataTim: DataTim = DataTim(), // Asumsikan Anda memiliki konstruktor default di DataTim
    val id_kuesioner: String = "",
    val isKoor: Boolean = false,
    val nama: String = "",
    val nim: String = "",
    val status: String = "",
    val wilayah: List<Wilayah> = emptyList()
)