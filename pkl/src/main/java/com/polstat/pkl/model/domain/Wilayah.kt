package com.polstat.pkl.model.domain

import java.util.Date

data class Wilayah(
    val noBS: String = "[not set]",
    val idKab: String = "[not set]",
    val namaKab: String = "[not set]",
    val idKec: String = "[not set]",
    val namaKec: String = "[not set]",
    val idKel: String = "[not set]",
    val namaKel: String = "[not set]",
    val jmlKlg: Int = -1,
    val jmlKlgEgb: Int = -1,
    val jmlRuta: Int = -1,
    val jmlRutaEgb: Int = -1,
    val tglListing: Date? = Date(),
    val tglPeriksa: Date? = Date(),
    val catatan: String = "[not set]",
    val status: String = "[not set]",
    val keluarga: List<Keluarga> = emptyList()
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/
