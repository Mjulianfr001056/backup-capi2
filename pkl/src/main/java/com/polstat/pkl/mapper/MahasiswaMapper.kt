package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.model.domain.Mahasiswa
import com.polstat.pkl.model.domain.Wilayah

fun Mahasiswa.toMahasiswaEntity(): MahasiswaEntity {
    return MahasiswaEntity(
        nim = nim,
        nama = nama,
        no_hp = no_hp
    )
}