package com.polstat.pkl.model.domain

import java.time.Instant

data class Session (
    val nama: String? = "",
    val nim: String? = "",
    val avatar: String? = "",
    val isKoor: Boolean? = false,
    val id_kuesioner: String? = "",
    val idTim: String? = "",
    val token: String? = "",
    val password: String? = ""
)