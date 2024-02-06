package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.KeluargaDto
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.domain.RutaDto

fun Keluarga.toKeluargaEntity(): KeluargaEntity {
    return KeluargaEntity(
        kodeKlg = kodeKlg,
        banjar = SLS,
        noBgFisik = noBgFisik,
        noBgSensus = noBgSensus,
        noSegmen = noSegmen,
        noUrutKlg = noUrutKlg,
        noUrutKlgEgb = noUrutKlgEgb,
        namaKK = namaKK,
        alamat = alamat,
        isGenzOrtu = isGenzOrtu,
        penglMkn = penglMkn,
        idBS = idBS,
        nimPencacah = nimPencacah,
        status = status
    )
}

fun KeluargaEntity.toKeluarga(
    ruta: List<Ruta> = emptyList()
): Keluarga {
    return Keluarga(
        kodeKlg = kodeKlg,
        SLS = banjar,
        noBgFisik = noBgFisik,
        noBgSensus = noBgSensus,
        noSegmen = noSegmen,
        noUrutKlg = noUrutKlg,
        noUrutKlgEgb = noUrutKlgEgb,
        namaKK = namaKK,
        alamat = alamat,
        isGenzOrtu = isGenzOrtu,
        penglMkn = penglMkn,
        idBS = idBS,
        nimPencacah = nimPencacah,
        ruta = ruta,
        status = status
    )
}

fun KeluargaEntity.toKeluargaDto(
    ruta: List<RutaDto>?
) : KeluargaDto {
    return KeluargaDto(
        SLS = banjar,
        alamat = alamat,
        is_genz_ortu = isGenzOrtu,
        kode_klg = kodeKlg,
        nama_kk = namaKK,
        no_bg_fisik = noBgFisik,
        no_bg_sensus = noBgSensus,
        id_bs = idBS,
        no_segmen = noSegmen,
        no_urut_klg = noUrutKlg,
        no_urut_klg_egb = noUrutKlgEgb,
        ruta = ruta,
        nim_pencacah = nimPencacah,
        status = status
    )
}

