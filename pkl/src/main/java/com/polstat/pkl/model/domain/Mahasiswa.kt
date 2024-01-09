package com.polstat.pkl.model.domain

data class Mahasiswa(
    val alamat: String = "",
    val email: String = "",
    val foto: String = "",
    val id_tim: String = "",
    val isKoor: Boolean = false,
    val nama: String = "",
    val nim: String = "",
    val no_hp: String = "",
    val password: String = "",
    val wilayah_kerja: List<Wilayah> = emptyList()
)
//{
//    // Konstruktor default
//    constructor() : this(
//        alamat = "",
//        email = "",
//        foto = "",
//        id_tim = "",
//        isKoor = false,
//        nama = "",
//        nim = "",
//        no_hp = "",
//        password = "",
//        wilayah = emptyList()
//    )
//}