package com.polstat.pkl.model.domain

data class SampelRuta(
    var idBS: String = "[not set]",
    var kodeRuta: String = "[not set]",
    var SLS: String = "[not set]",
    var noSegmen: String = "[not set]",
    var noBgFisik: String = "[not set]",
    var noBgSensus: String = "[not set]",
    var noUrutKlg: String = "[not set]",
    var noUrutRuta: Int = -1,
    var noUrutRutaEgb: Int = -1,
    var genzOrtuKeluarga: String = "[not set]",
    var alamat: String = "[not set]",
    var namaKrt: String = "[not set]",
    var genzOrtuRuta: Int = -1,
    var long: Double = -1.0,
    var lat: Double = -1.0,
    var status: String = "[not set]"
)