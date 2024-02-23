package com.polstat.pkl.model.domain

data class Session (
    //Profile
    val nama: String = "",
    val nim: String = "",
    val avatar: String = "",
    val isKoor: Boolean = false,
    val token: String = "",

    //Tim
    val idTim: String = "",
    val namaTim: String = "",

    //PML
    val namaPml: String = "",
    val nimPml: String = "",
    val noTlpPml: String = "",
)