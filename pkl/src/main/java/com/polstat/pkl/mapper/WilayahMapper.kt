package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.model.domain.Ruta
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
        jmlGenZ = jmlGenZ,
        jmlRt = jmlRt,
        jmlRtGenz = jmlRtGenz,
        status = status,
        tglListing = tglListing,
        tglPeriksa = tglPeriksa,
        nim = nim
    )
}

fun WilayahEntity.toWilayah(
    ruta: List<Ruta>
): Wilayah {
    return Wilayah(
        catatan = catatan,
        idKab = idKab,
        idKec = idKec,
        idKel = idKel,
        jmlGenZ = jmlGenZ,
        jmlRt = jmlRt,
        jmlRtGenz = jmlRtGenz,
        namaKab = namaKab,
        namaKec = namaKec,
        namaKel = namaKel,
        noBS = noBS,
        ruta = ruta,
        status = status,
        tglListing = tglListing,
        tglPeriksa = tglPeriksa
    )
}