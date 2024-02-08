package com.polstat.pkl.model.domain

data class Ruta(
    val kodeRuta: String = "[not set]",
    val noUrutRuta: String = "[not set]",
    val noUrutEgb: Int? = -1,
    val kkOrKrt: String = "[not set]",
    val namaKrt: String = "[not set]",
    val isGenzOrtu: Int = -1,
    val katGenz: Int =-1,
    val long: Double = -1.0,
    val lat: Double = -1.0,
    val catatan: String = "[not set]",
    val idBS: String = "[not set]",
    val nimPencacah: String = "[not set]",
    val status: String = "[not set]"
)