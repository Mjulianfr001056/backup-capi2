package com.polstat.pkl.model.domain

data class Mahasiswa(
    val alamat: String,
    val email: String,
    val foto: String,
    val id_tim: String,
    val isKoor: Boolean,
    val nama: String,
    val nim: String,
    val no_hp: String,
    val password: String,
    val wilayah: Wilayah
)