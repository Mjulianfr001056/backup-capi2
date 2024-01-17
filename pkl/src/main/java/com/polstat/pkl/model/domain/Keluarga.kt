package com.polstat.pkl.model.domain

data class Keluarga (
    val kodeKlg: String = "",
    val SLS: String? = "",
    val noBgFisik: Int? = 0,
    val noBgSensus: Int? = 0,
    val noSegmen: String? = "",
    val noUrutKlg: Int? = 0,
    val noUrutKlgEgb: Int? = 0,
    val namaKK: String? = "",
    val alamat: String? = "",
    val isGenzOrtu: Int? = 0,
    val penglMkn: Int? = 0,
    val noBS: String? = "",
    val ruta: List<Ruta>
)