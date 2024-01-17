package com.polstat.pkl.model.domain

data class Mahasiswa(
    val alamat: String? = "",
    val email: String? = "",
    val foto: String? = "",
    val idTim: String? = "",
    val isKoor: Boolean? = false,
    val nama: String? = "",
    val nim: String = "",
    val noHp: String? = "",
    val password: String? = "",
    val wilayahKerja: List<Wilayah>? = emptyList()
)