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
){
    constructor(
        foto: String,
        id_tim: String,
        isKoor: Boolean,
        nama: String,
        nim: String,
    ) : this(
        alamat = "",
        email = "",
        foto = foto,
        id_tim = id_tim,
        isKoor = isKoor,
        nama = nama,
        nim = nim,
        no_hp = "",
        password = "",
        wilayah_kerja = emptyList()
    )
}

/** QC 23/Jan/2024
 * Buat secondary constructor untuk data yang tidak wajib diisi
 * TODO(Cek lagi apakah sebaiknya nyimpan data yang wajib diisi aja?)
 *
 * Buat non-nullables semua karena udah dikasih default value
 **/