package com.polstat.pkl.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.database.entity.WilayahEntity

data class MahasiswaWithWilayah (
    @Embedded
    val mahasiswa: MahasiswaEntity,
    @Relation(
        parentColumn = "nim",
        entityColumn = "nim"
    )
    val listWilayah: List<WilayahEntity> = emptyList()
)