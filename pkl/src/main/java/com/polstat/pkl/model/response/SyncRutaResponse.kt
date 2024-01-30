package com.polstat.pkl.model.response

import com.polstat.pkl.model.domain.Keluarga
import java.util.Date

data class SyncRutaResponse(
    val noBS: String = "",
    val idKab: String? = "",
    val namaKab: String? = "",
    val idKec: String? = "",
    val namaKec: String? = "",
    val idKel: String? = "",
    val namaKel: String? = "",
    val jmlKlg: Int? = 0,
    val jmlKlgEgb: Int? = 0,
    val jmlRuta: Int? = 0,
    val jmlRutaEgb: Int? = 0,
    val tglListing: Date? = Date(),
    val tglPeriksa: Date? = Date(),
    val catatan: String? = "",
    val status: String? = "",
    val keluarga: List<Keluarga>? = emptyList()
)