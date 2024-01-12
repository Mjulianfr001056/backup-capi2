package com.polstat.pkl.database.relation

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.database.entity.WilayahEntity

data class MahasiswaWithWilayah (
    @Embedded
    val mahasiswa: MahasiswaEntity? = MahasiswaEntity(),
    @Relation(
        parentColumn = "nim",
        entityColumn = "nim"
    )
    val listWilayah: List<WilayahEntity>? = emptyList()
)