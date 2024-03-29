package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.model.response.FinalisasiBSResponse
import com.polstat.pkl.model.response.SyncRutaResponse

fun Wilayah.toWilayahEntity(): WilayahEntity {
    return WilayahEntity(
        idBS = idBS,
        noBS = noBS,
        namaKab = namaKab,
        namaKec = namaKec,
        namaKel = namaKel,
        jmlKlg = jmlKlg,
        jmlKlgEgb = jmlKlgEgb,
        jmlRuta = jmlRuta,
        jmlRutaEgb = jmlRutaEgb,
        status = status,
        tglListing = tglListing,
        tglPeriksa = tglPeriksa
    )
}

//fun WilayahEntity.toWilayah(
//    keluarga: List<Keluarga>
//): Wilayah {
//    return Wilayah(
//        idBS = idBS,
//        noBS = noBS,
//        namaKab = namaKab,
//        namaKec = namaKec,
//        namaKel = namaKel,
//        jmlKlg = jmlKlg,
//        jmlKlgEgb = jmlKlgEgb,
//        jmlRuta = jmlRuta,
//        jmlRutaEgb = jmlRutaEgb,
//        status = status,
//        tglListing = tglListing,
//        tglPeriksa = tglPeriksa,
//        keluarga = keluarga
//    )
//}

fun SyncRutaResponse.toWilayah(): Wilayah {
    return Wilayah(
        idBS = idBS,
        noBS = noBS,
        idKab = idKab,
        namaKab = namaKab,
        idKec = idKec,
        namaKec = namaKec,
        idKel = idKel,
        namaKel = namaKel,
        jmlKlg = jmlKlg,
        jmlKlgEgb = jmlKlgEgb,
        jmlRuta = jmlRuta,
        jmlRutaEgb = jmlRutaEgb,
        tglListing = tglListing,
        tglPeriksa = tglPeriksa,
        catatan = catatan,
        status = status,
        keluarga = keluarga
    )
}

fun FinalisasiBSResponse.toWilayah(): Wilayah {
    return Wilayah(
        idBS = idBS,
        noBS = noBS,
        idKab = idKab,
        namaKab = namaKab,
        idKec = idKec,
        namaKec = namaKec,
        idKel = idKel,
        namaKel = namaKel,
        jmlKlg = jmlKlg,
        jmlKlgEgb = jmlKlgEgb,
        jmlRuta = jmlRuta,
        jmlRutaEgb = jmlRutaEgb,
        tglListing = tglListing,
        tglPeriksa = tglPeriksa,
        catatan = catatan,
        status = status,
        keluarga = keluarga
    )
}