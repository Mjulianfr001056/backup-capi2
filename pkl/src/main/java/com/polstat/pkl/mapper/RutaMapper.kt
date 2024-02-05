package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.domain.RutaDto

fun Ruta.toRutaEntity(): RutaEntity {
    return RutaEntity(
        kodeRuta = kodeRuta,
        idBS = idBS,
        noUrutRuta = noUrutRuta,
        noUrutEgb = noUrutEgb,
        kkOrKrt = kkOrKrt,
        namaKrt = namaKrt,
        catatan = catatan,
        genzOrtu = isGenzOrtu,
        katGenz = katGenz,
        lat = lat,
        long = long,
        nimPencacah = nimPencacah,
        status = status
    )
}

fun RutaEntity.toRuta(): Ruta {
    return Ruta(
        kodeRuta = kodeRuta,
        idBS = idBS,
        noUrutRuta = noUrutRuta,
        noUrutEgb = noUrutEgb,
        kkOrKrt = kkOrKrt,
        namaKrt = namaKrt,
        catatan = catatan,
        isGenzOrtu = genzOrtu,
        katGenz = katGenz,
        lat = lat,
        long = long,
        nimPencacah = nimPencacah,
        status = status
    )
}

fun RutaEntity.toRutaDto(): RutaDto {
    return RutaDto(
        catatan = catatan,
        is_genz_ortu = genzOrtu,
        kode_ruta = kodeRuta,
        lat = lat,
        long = long,
        nama_krt = namaKrt,
        id_bs = idBS,
        no_urut_ruta = noUrutRuta,
        kat_genz = katGenz,
        kk_or_krt = kkOrKrt,
        nim_pencacah = nimPencacah,
        status = status
    )
}

fun List<RutaEntity>.toRutaDtoList(): List<RutaDto> {
    return this.map { it.toRutaDto() }
}