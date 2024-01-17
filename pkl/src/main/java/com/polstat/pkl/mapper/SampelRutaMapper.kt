package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.model.domain.SampelRuta

fun SampelRuta.toSampelRutaEntity(): SampelRutaEntity {
    return SampelRutaEntity(
        kodeRuta = kodeRuta,
        noBS = noBS,
        noUrutRuta = noUrutRuta,
        kkOrKrt = kkOrKrt,
        namaKrt = namaKrt,
        catatan = catatan,
        genzOrtu = isGenzOrtu,
        katGenz = katGenz,
        lat = lat,
        long = long,
        status = status
    )
}

fun SampelRutaEntity.toSampelRuta(): SampelRuta {
    return SampelRuta(
        kodeRuta = kodeRuta,
        noBS = noBS,
        noUrutRuta = noUrutRuta,
        kkOrKrt = kkOrKrt,
        namaKrt = namaKrt,
        catatan = catatan,
        isGenzOrtu = genzOrtu,
        katGenz = katGenz,
        lat = lat,
        long = long,
        status = status
    )
}