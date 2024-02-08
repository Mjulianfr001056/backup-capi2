package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.model.domain.SampelRuta

fun SampelRuta.toSampelRutaEntity(): SampelRutaEntity {
    return SampelRutaEntity(
        idBS = idBS,
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