package com.polstat.pkl.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.polstat.pkl.database.entity.KeluargaAndRutaEntity
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity

data class RutaWithKeluarga(
    @Embedded val ruta: RutaEntity = RutaEntity(),
    @Relation(
        entity = KeluargaEntity::class,
        parentColumn = "kodeRuta",
        entityColumn = "kodeKlg",
        associateBy = Junction(KeluargaAndRutaEntity::class)
    )
    val listKeluarga: List<KeluargaEntity> = emptyList()
)