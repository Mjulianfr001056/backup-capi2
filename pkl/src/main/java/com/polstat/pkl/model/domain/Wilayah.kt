package com.polstat.pkl.model.domain

data class Wilayah(
    val catatan: String,
    val idKab: String,
    val idKec: String,
    val idKel: String,
    val jmlGenZ: Int,
    val jmlRt: Int,
    val jmlRtGenz: Int,
    val namaKab: String,
    val namaKec: String,
    val namaKel: String,
    val noBS: String,
    val ruta: List<Ruta>,
    val status: String,
    val tglListing: Any,
    val tglPeriksa: Any
)