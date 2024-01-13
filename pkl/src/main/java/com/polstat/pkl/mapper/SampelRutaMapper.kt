package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.model.domain.SampelRuta

fun SampelRuta.toSampelRutaEntity(): SampelRutaEntity {
    return SampelRutaEntity(
        kodeRuta = kodeRuta,
        noBS = noBS,
        noBgFisik = noBgFisik,
        noBgSensus = noBgSensus,
        noSegmen = noSegmen,
        noUrutRtEgb = noUrutRtEgb,
        noUrutRuta = noUrutRuta,
        namaKrt = namaKrt,
        alamat = alamat,
        catatan = catatan,
        genzOrtu = isGenzOrtu,
        jmlGenz = jmlGenz,
        lat = lat,
        long = long,
    )
}

fun SampelRutaEntity.toSampelRuta(): SampelRuta {
    return SampelRuta(
        alamat = alamat,
        catatan = catatan,
        isGenzOrtu = genzOrtu,
        jmlGenz = jmlGenz,
        kodeRuta = kodeRuta,
        lat = lat,
        long = long,
        namaKrt = namaKrt,
        noBS = noBS,
        noBgFisik = noBgFisik,
        noBgSensus = noBgSensus,
        noSegmen = noSegmen,
        noUrutRtEgb = noUrutRtEgb,
        noUrutRuta = noUrutRuta,
    )
}