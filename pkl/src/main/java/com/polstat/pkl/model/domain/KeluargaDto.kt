package com.polstat.pkl.model.domain

data class KeluargaDto (
    val SLS: String?,
    val alamat: String?,
    val is_genz_ortu: Int?,
    val kode_klg: String?,
    val nama_kk: String?,
    val no_bg_fisik: String?,
    val no_bg_sensus: String?,
    val id_bs: String?,
    val no_segmen: String?,
    val no_urut_klg: String?,
    val no_urut_klg_egb: Int?,
    val ruta: List<RutaDto>?,
    val nim_pencacah: String?,
    val status: String?
)