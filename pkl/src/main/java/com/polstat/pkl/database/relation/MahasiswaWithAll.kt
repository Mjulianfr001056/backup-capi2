package com.polstat.pkl.database.relation

import com.polstat.pkl.database.entity.MahasiswaEntity

data class MahasiswaWithAll (
    val mahasiswaWithWilayah: MahasiswaWithWilayah? = MahasiswaWithWilayah(),
    val listWilayahWithRuta: List<WilayahWithRuta>? = emptyList()
)