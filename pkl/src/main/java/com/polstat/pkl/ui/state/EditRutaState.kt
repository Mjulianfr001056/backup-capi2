package com.polstat.pkl.ui.state

data class EditRutaState(
    val SLS: String = "",
    val SLSError: String? = null,
    val noSegmen: String = "S000",
    val noSegmenError: String? = null,
    val noBgFisik: String = "",
    val noBgFisikError: String? = null,
    val noBgSensus: String = "",
    val noBgSensusError: String? = null,
    val noUrutKlg: String = "",
    val noUrutKlgError: String? = null,
    val namaKK: String = "",
    val namaKKError: String? = null,
    val alamat: String = "",
    val alamatError: String? = null,
    val isGenzOrtu: Int = 0,
    val isGenzOrtuError: String? = null,
    val noUrutKlgEgb: Int = 0,
    val noUrutKlgEgbError: String? = null,
    val penglMkn: Int = 0,
    val penglMknError: String? = null,
    val noUrutRuta: Int = 0,
    val noUrutRutaError: String? = null,
    val kkOrKrt: String = "",
    val kkOrKrtError: String? = null,
    val namaKrt: String = "",
    val namaKrtError: String? = null,
    val genzOrtu: Int = 0,
    val genzOrtuError: String? = null,
    val katGenz: Int = 0,
    val katGenzError: String? = null,
    val kodeRuta: String = "",
    val kodeRutaError: String? = null,
    val long: Double = 0.0,
    val longError: String? = null,
    val lat: Double = 0.0,
    val latError: String? = null,
)