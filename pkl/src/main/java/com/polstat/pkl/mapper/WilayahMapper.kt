package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Wilayah

fun Wilayah.toWilayahEntity(
    nim: String
): WilayahEntity {
    return WilayahEntity(
        noBS = noBS,
        idKab = idKab,
        idKec = idKec,
        idKel = idKel,
        namaKab = namaKab,
        namaKec = namaKec,
        namaKel = namaKel,
        catatan = catatan,
        jmlRt = jmlRt,
        status = status,
        tglListing = tglListing,
        tglPeriksa = tglPeriksa,
        nim = nim
    )
}

fun WilayahEntity.toWilayah(
    keluarga: List<Keluarga>
): Wilayah {
    return Wilayah(
        noBS = noBS,
        idKab = idKab,
        idKec = idKec,
        idKel = idKel,
        namaKab = namaKab,
        namaKec = namaKec,
        namaKel = namaKel,
        catatan = catatan,
        jmlRt = jmlRt,
        status = status,
        tglListing = tglListing,
        tglPeriksa = tglPeriksa,
        keluarga = keluarga
    )
}