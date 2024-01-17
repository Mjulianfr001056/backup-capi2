package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.KeluargaDto
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.domain.RutaDto

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

fun KeluargaEntity.toKeluargaDto(
    ruta: List<RutaDto>?
) : KeluargaDto {
    return KeluargaDto(
        SLS = SLS,
        alamat = alamat,
        is_genz_ortu = isGenzOrtu,
        kode_klg = kodeKlg,
        nama_kk = namaKK,
        no_bg_fisik = noBgFisik,
        no_bg_sensus = noBgSensus,
        no_bs = noBS,
        no_segmen = noSegmen,
        no_urut_klg = noUrutKlg,
        no_urut_klg_egb = noUrutKlgEgb,
        ruta = ruta,
        status = status
    )
}
