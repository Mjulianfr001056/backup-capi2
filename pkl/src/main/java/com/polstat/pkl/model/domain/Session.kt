package com.polstat.pkl.model.domain

data class Session(
    val user: User,
    val dataTim: DataTim,
    val wilayah: List<Wilayah>
)
