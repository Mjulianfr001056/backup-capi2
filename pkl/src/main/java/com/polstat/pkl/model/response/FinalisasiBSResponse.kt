package com.polstat.pkl.model.response

import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Ruta
import java.util.Date

data class FinalisasiBSResponse(
//    val count: Int? = 0,
//    val data: List<Ruta>? = emptyList(),
//    val status: String? = ""
    val catatan: String = "",
    val idBS: String = "",
    val idKab: String = "",
    val idKec: String = "",
    val idKel: String = "",
    val jmlKlg: Int = 0,
    val jmlKlgEgb: Int = 0,
    val jmlRuta: Int = 0,
    val jmlRutaEgb:  Int = 0,
    val keluarga: List<Keluarga> = emptyList(),
    val namaKab: String = "",
    val namaKec: String = "",
    val namaKel: String = "",
    val noBS: String = "",
    val status: String = "",
    val tglListing: Date? = Date(),
    val tglPeriksa: Date? = Date()
)