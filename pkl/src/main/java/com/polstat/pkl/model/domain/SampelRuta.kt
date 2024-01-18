package com.polstat.pkl.model.domain

data class SampelRuta(
    var noBS: String? = "",
    var kodeRuta: String = "",
    var SLS: String? = "",
    var noSegmen: String? = "",
    var noBgFisik: String? = "",
    var noBgSensus: String? = "",
    var noUrutKlg: String? = "",
    var noUrutRuta: Int? = 0,
    var noUrutRutaEgb: Int? = 0,
    var genzOrtuKeluarga: String? = "",
    var alamat: String? = "",
    var namaKrt: String? = "",
    var genzOrtuRuta: Int? = 0,
    var long: Double? = 0.0,
    var lat: Double? = 0.0,
    var status: String? = ""
)