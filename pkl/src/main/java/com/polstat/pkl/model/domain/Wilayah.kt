package com.polstat.pkl.model.domain

import java.util.Date

data class Wilayah(
    val catatan: String = "",
    val idKab: String = "",
    val idKec: String = "",
    val idKel: String = "",
    val jmlGenZ: Int = 0,
    val jmlRt: Int = 0,
    val jmlRtGenz: Int = 0,
    val namaKab: String = "",
    val namaKec: String = "",
    val namaKel: String = "",
    val noBS: String = "",
    val ruta: List<Ruta> = emptyList(),
    val status: String = "",
    val tglListing: Date = Date(),
    val tglPeriksa: Date = Date()
)