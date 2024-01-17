package com.polstat.pkl.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.polstat.pkl.database.entity.KeluargaAndRutaEntity
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity

data class KeluargaWithRuta (
    @Embedded
    val keluarga: KeluargaEntity,
    @Relation(
        parentColumn = "kodeKlg",
        entity = RutaEntity::class,
        entityColumn = "kodeRuta",
        associateBy = Junction(
            value = KeluargaAndRutaEntity::class,
            parentColumn = "kodeKlg",
            entityColumn = "kodeRuta"
        )
    )
    val listRuta: List<RutaEntity>
)