package com.polstat.pkl.database.relation

data class WilayahWithAll (
    val wilayahWithKeluarga: WilayahWithKeluarga? = WilayahWithKeluarga(),
    val wilayahWithRuta: WilayahWithRuta? = WilayahWithRuta(),
    val listKeluargaWithRuta: List<KeluargaWithRuta>? = emptyList(),
    val listRutaWithKeluarga: List<RutaWithKeluarga>? = emptyList()
)