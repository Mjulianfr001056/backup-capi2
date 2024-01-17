package com.polstat.pkl.database.relation


data class MahasiswaWithAll (
    val mahasiswaWithWilayah: MahasiswaWithWilayah? = MahasiswaWithWilayah(),
    val listWilayahWithAll: List<WilayahWithAll>? = emptyList()
)