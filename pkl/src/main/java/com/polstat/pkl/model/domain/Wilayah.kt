package com.polstat.pkl.model.domain

import java.util.Date

data class Wilayah(
    val noBS: String = "",
    val idKab: String? = "",
    val namaKab: String? = "",
    val idKec: String? = "",
    val namaKec: String? = "",
    val idKel: String? = "",
    val namaKel: String? = "",
    val jmlRt: Int? = 0,
    val tglListing: Date? = Date(),
    val tglPeriksa: Date? = Date(),
    val catatan: String? = "",
    val status: String? = "",
    val keluarga: List<Keluarga>? = emptyList()
)