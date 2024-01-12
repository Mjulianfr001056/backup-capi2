package com.polstat.pkl.model.domain

data class Ruta(
    val alamat: String? = "",
    val catatan: String? = "",
    val isGenzOrtu: String? = "",
    val jmlGenz: Int? = 0,
    val kodeRuta: String = "",
    val lat: Double? = 0.0,
    val long: Double? = 0.0,
    val namaKrt: String? = "",
    val noBS: String? = "",
    val noBgFisik: Int? = 0,
    val noBgSensus: Int? = 0,
    val noSegmen: Int? = 0,
    val noUrutRtEgb: Int? = 0,
    val noUrutRuta: Int? = 0,
    val status: String? = ""
)