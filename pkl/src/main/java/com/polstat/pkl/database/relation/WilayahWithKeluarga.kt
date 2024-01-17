package com.polstat.pkl.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.WilayahEntity

data class WilayahWithKeluarga (
    @Embedded
    val wilayah: WilayahEntity? = WilayahEntity(),
    @Relation(
        parentColumn = "noBS",
        entityColumn = "noBS"
    )
    val listKeluarga: List<KeluargaEntity>? = emptyList()
)