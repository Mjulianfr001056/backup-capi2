package com.polstat.pkl.model.domain

//single instance aja

data class DataTim(
    val anggota: List<Mahasiswa> = emptyList(),
    val idTim: String = "[not set]",
    val namaTim: String = "[not set]",
    val passPML: String = "[not set]",
    val namaPML: String = "[not set]",
    val nimPML: String = "[not set]",
    val teleponPML: String = "[not set]"
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/