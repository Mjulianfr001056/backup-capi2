package com.polstat.pkl.mapper

import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.domain.RutaDto

class RutaMapper {

    fun mapToRutaDto(ruta: Ruta): RutaDto {
        return RutaDto(
            alamat = ruta.alamat,
            catatan = ruta.catatan,
            is_genz_ortu = ruta.isGenzOrtu,
            jml_genz = ruta.jmlGenz,
            kode_ruta = ruta.kodeRuta,
            lat = ruta.lat,
            long = ruta.long,
            nama_krt = ruta.namaKrt,
            no_bg_fisik = ruta.noBgFisik,
            no_bg_sensus = ruta.noBgSensus,
            no_bs = ruta.noBS,
            no_segmen = ruta.noSegmen,
            no_urut_ruta = ruta.noUrutRuta,
            status = ruta.status
        )
    }

}