package com.polstat.pkl.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.entity.WilayahEntity

data class WilayahWithRuta (
    @Embedded
    val wilayah: WilayahEntity? = WilayahEntity(),
    @Relation(
        parentColumn = "noBS",
        entityColumn = "kodeRuta"
    )
    val listRuta: List<RutaEntity>? = emptyList()
)