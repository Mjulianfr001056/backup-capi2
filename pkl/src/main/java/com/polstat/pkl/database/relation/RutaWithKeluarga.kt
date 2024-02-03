package com.polstat.pkl.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.polstat.pkl.database.entity.KeluargaAndRutaEntity
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity

data class RutaWithKeluarga (
    @Embedded
    val ruta: RutaEntity,
    @Relation(
        parentColumn = "kodeRuta",
        entity = KeluargaEntity::class,
        entityColumn = "kodeKlg",
        associateBy = Junction(
            value = KeluargaAndRutaEntity::class,
            parentColumn = "kodeRuta",
            entityColumn = "kodeKlg"
        )
    )
    val listKeluarga: List<KeluargaEntity>
)