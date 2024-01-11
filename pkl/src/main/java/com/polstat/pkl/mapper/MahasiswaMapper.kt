package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.model.domain.Mahasiswa
import com.polstat.pkl.model.domain.Wilayah

fun Mahasiswa.toMahasiswaEntity(): MahasiswaEntity {
    return MahasiswaEntity(
        nim = nim,
        nama = nama,
        isKoor = isKoor,
        password = password,
        alamat = alamat,
        email = email,
        foto = foto,
        id_tim = id_tim,
        no_hp = no_hp
    )
}

fun MahasiswaEntity.toMahasiswa(
    wilayah: List<Wilayah>
): Mahasiswa {
    return Mahasiswa(
        alamat = alamat,
        email = email,
        foto = foto,
        id_tim = id_tim,
        isKoor = isKoor,
        nama = nama,
        nim = nim,
        no_hp = no_hp,
        password = password,
        wilayah_kerja = wilayah // Sesuaikan dengan kebutuhan, saat ini diatur ke emptyList()
    )
}