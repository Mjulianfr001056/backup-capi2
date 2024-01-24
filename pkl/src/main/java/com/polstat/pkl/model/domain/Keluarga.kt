package com.polstat.pkl.model.domain

data class Keluarga (
    val kodeKlg: String = "[not set]",
    val SLS: String = "[not set]",
    val noBgFisik: Int = -1,
    val noBgSensus: Int = -1,
    val noSegmen: String = "[not set]",
    val noUrutKlg: Int = -1,
    val noUrutKlgEgb: Int = -1,
    val namaKK: String = "[not set]",
    val alamat: String = "[not set]",
    val isGenzOrtu: Int = -1,
    val penglMkn: Int = -1,
    val noBS: String = "[not set]",
    val ruta: List<Ruta> = emptyList(),
    val status: String = "[not set]"
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/