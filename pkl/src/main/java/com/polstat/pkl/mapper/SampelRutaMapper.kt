package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.model.domain.SampelRuta

fun SampelRuta.toSampelRutaEntity(): SampelRutaEntity {
    return SampelRutaEntity(
        noBS = noBS,
        kodeRuta = kodeRuta,
        SLS = SLS,
        noSegmen = noSegmen,
        noBgFisik = noBgFisik,
        noBgSensus = noBgSensus,
        noUrutKlg = noUrutKlg,
        noUrutRuta = noUrutRuta,
        noUrutRutaEgb = noUrutRutaEgb,
        genzOrtuKeluarga = genzOrtuKeluarga,
        alamat = alamat,
        namaKrt = namaKrt,
        genzOrtuRuta = genzOrtuRuta,
        long = long,
        lat = lat,
        status = status
    )
}

fun SampelRutaEntity.toSampelRuta(): SampelRuta {
    return SampelRuta(
        noBS = noBS,
        kodeRuta = kodeRuta,
        SLS = SLS,
        noSegmen = noSegmen,
        noBgFisik = noBgFisik,
        noBgSensus = noBgSensus,
        noUrutKlg = noUrutKlg,
        noUrutRuta = noUrutRuta,
        noUrutRutaEgb = noUrutRutaEgb,
        genzOrtuKeluarga = genzOrtuKeluarga,
        alamat = alamat,
        namaKrt = namaKrt,
        genzOrtuRuta = genzOrtuRuta,
        long = long,
        lat = lat,
        status = status
    )
}