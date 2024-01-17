package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Ruta

fun Keluarga.toKeluargaEntity(): KeluargaEntity {
    return KeluargaEntity(
        kodeKlg = kodeKlg,
        SLS = SLS,
        noBgFisik = noBgFisik,
        noBgSensus = noBgSensus,
        noSegmen = noSegmen,
        noUrutKlg = noUrutKlg,
        noUrutKlgEgb = noUrutKlgEgb,
        namaKK = namaKK,
        alamat = alamat,
        isGenzOrtu = isGenzOrtu,
        penglMkn = penglMkn,
        noBS = noBS
    )
}

fun KeluargaEntity.toKeluarga(
    ruta: List<Ruta> = emptyList()
): Keluarga {
    return Keluarga(
        kodeKlg = kodeKlg,
        SLS = SLS,
        noBgFisik = noBgFisik,
        noBgSensus = noBgSensus,
        noSegmen = noSegmen,
        noUrutKlg = noUrutKlg,
        noUrutKlgEgb = noUrutKlgEgb,
        namaKK = namaKK,
        alamat = alamat,
        isGenzOrtu = isGenzOrtu,
        penglMkn = penglMkn,
        noBS = noBS,
        ruta = ruta
    )
}
