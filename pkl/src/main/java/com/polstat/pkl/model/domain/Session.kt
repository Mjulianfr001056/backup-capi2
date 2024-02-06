package com.polstat.pkl.model.domain

data class Session (
    val nama: String = "",
    val nim: String = "",
    val avatar: String = "",
    val isKoor: Boolean = false,
    val token: String = "",

    //val id_kuesioner: String? = "",
    val idTim: String = "",
    val namaTim: String = "",
    //val password: String? = ""
)