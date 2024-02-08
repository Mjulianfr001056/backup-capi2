package com.polstat.pkl.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.entity.MahasiswaEntity

data class DataTimWithMahasiswa (
    @Embedded
    val dataTim: DataTimEntity? = DataTimEntity(),
    @Relation(
        parentColumn = "idTim",
        entityColumn = "nim"
    )
    val listMahasiswa: List<MahasiswaEntity>? = emptyList()
)