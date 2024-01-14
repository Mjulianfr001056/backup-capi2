package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.domain.RutaDto

fun Ruta.toRutaEntity(): RutaEntity {
    return RutaEntity(
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
        GenzOrtu = isGenzOrtu,
        jmlGenz = jmlGenz,
        lat = lat,
        long = long,
        status = status
    )
}

fun RutaEntity.toRuta(): Ruta {
    return Ruta(
        alamat = alamat,
        catatan = catatan,
        isGenzOrtu = GenzOrtu,
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
        status = status
    )
}

fun RutaEntity.toRutaDto(): RutaDto {
    return RutaDto(
        alamat = alamat,
        catatan = catatan,
        is_genz_ortu = GenzOrtu,
        jml_genz = jmlGenz,
        kode_ruta = kodeRuta,
        lat = lat,
        long = long,
        nama_krt = namaKrt,
        no_bg_fisik = noBgFisik,
        no_bg_sensus = noBgSensus,
        no_bs = noBS,
        no_segmen = noSegmen,
        no_urut_ruta = noUrutRuta,
        status = status
    )
}

fun List<RutaEntity>.toRutaDtoList(): List<RutaDto> {
    return this.map { it.toRutaDto() }
}