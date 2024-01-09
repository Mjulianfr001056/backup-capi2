package com.polstat.pkl.model.domain

import java.util.Date

import java.util.*

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
//{
//    // Konstruktor default
//    constructor() : this(
//        catatan = "",
//        idKab = "",
//        idKec = "",
//        idKel = "",
//        jmlGenZ = 0,
//        jmlRt = 0,
//        jmlRtGenz = 0,
//        namaKab = "",
//        namaKec = "",
//        namaKel = "",
//        noBS = "",
//        ruta = emptyList(),
//        status = "",
//        tglListing = Date(),
//        tglPeriksa = Date()
//    )
//}