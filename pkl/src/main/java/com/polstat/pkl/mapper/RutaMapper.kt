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
        jmlGenzAnak = jmlGenzAnak,
        jmlGenzDewasa = jmlGenzDewasa,
        katGenz = katGenz,
        latitude = lat,
        longitude = long,
        nimPencacah = nimPencacah,
        noSegmen = noSegmen,
        isEnable = isEnable,
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
        jmlGenzAnak = jmlGenzAnak,
        jmlGenzDewasa = jmlGenzDewasa,
        katGenz = katGenz,
        lat = latitude,
        long = longitude,
        nimPencacah = nimPencacah,
        noSegmen = noSegmen,
        isEnable = isEnable,
        status = status
    )
}

fun RutaEntity.toRutaDto(): RutaDto {
    return RutaDto(
        catatan = catatan,
        jml_genz_anak = jmlGenzAnak,
        jml_genz_dewasa = jmlGenzDewasa,
        kode_ruta = kodeRuta,
        lat = latitude,
        long = longitude,
        nama_krt = namaKrt,
        id_bs = idBS,
        no_urut_ruta = noUrutRuta,
        kat_genz = katGenz,
        kk_or_krt = kkOrKrt,
        nim_pencacah = nimPencacah,
        no_segmen = noSegmen,
        is_enable = isEnable,
        status = status
    )
}

fun List<RutaEntity>.toRutaDtoList(): List<RutaDto> {
    return this.map { it.toRutaDto() }
}

fun List<RutaEntity>.toRutaList(): List<Ruta> {
    return this.map { it.toRuta() }
}