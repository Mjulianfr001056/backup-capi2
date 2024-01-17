package com.polstat.pkl.model.domain

data class Ruta(
    val kodeRuta: String = "",
    val noUrutRuta: Int? = 0,
    val noUrutRutaEgb: Int? = 0,
    val kkOrKrt: String? = "",
    val namaKrt: String? = "",
    val isGenzOrtu: String? = "",
    val katGenz: String? = "",
    val long: Double? = 0.0,
    val lat: Double? = 0.0,
    val catatan: String? = "",
    val noBS: String? = "",
    val status: String? = ""
)