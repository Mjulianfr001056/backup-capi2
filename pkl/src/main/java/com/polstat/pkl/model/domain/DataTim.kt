package com.polstat.pkl.model.domain

data class DataTim(
    val anggota: List<Mahasiswa> = emptyList(),
    val idTim: String = "",
    val namaTim: String = "",
    val passPML: String = "",
    val namaPML: String = "",
    val nimPML: String = "",
    val teleponPML: String = ""
)
//{
//    // Konstruktor default
//    constructor() : this(
//        anggota = emptyList(),
//        idTim = "",
//        namaTim = "",
//        passPML = "",
//        namaPML = "",
//        nimPML = "",
//        teleponPML = ""
//    )
//}