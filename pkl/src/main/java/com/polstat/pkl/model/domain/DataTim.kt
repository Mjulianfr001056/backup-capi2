package com.polstat.pkl.model.domain

data class DataTim(
    val anggota: List<Mahasiswa>,
    val idTim: String,
    val namaTim: String,
    val passPML: String,
    val namaPML: String,
    val nimPML: String,
    val teleponPML: String
)