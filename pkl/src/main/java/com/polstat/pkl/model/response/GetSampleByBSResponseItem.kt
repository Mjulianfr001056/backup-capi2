package com.polstat.pkl.model.response

data class GetSampleByBSResponseItem(
    val alamat: String,
    val catatan: String,
    val isGenzOrtu: String,
    val jmlGenz: Int,
    val kodeRuta: String,
    val lat: Double,
    val long: Double,
    val namaKrt: String,
    val noBS: String,
    val noBgFisik: Int,
    val noBgSensus: Int,
    val noSegmen: Int,
    val noUrutRtEgb: Int,
    val noUrutRuta: Int
)