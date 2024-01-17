package com.polstat.pkl.database.relation

data class WilayahWithAll (
    val wilayahWithKeluarga: WilayahWithKeluarga? = WilayahWithKeluarga(),
    val listKeluargaWithRuta: List<KeluargaWithRuta>? = emptyList()
)