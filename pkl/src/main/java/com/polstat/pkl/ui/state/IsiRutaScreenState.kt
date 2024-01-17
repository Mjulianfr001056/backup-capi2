package com.polstat.pkl.ui.state

data class IsiRutaScreenState(
    val SLS: String = "",
    val SLSError: String? = null,
    val noSegmen: String = "S001",
    val noSegmenError: String? = null,
    val noBgFisik: String = "001",
    val noBgFisikError: String? = null,
    val noBgSensus: String = "001",
    val noBgSensusError: String? = null,
    val noUrutKlg: Int? = 0,
    val noUrutKlgError: String? = null,
    val namaKK: String = "",
    val namaKKError: String? = null,
    val alamat: String = "",
    val alamatError: String? = null,
    val isGenzOrtu: Int? = 0,
    val isGenzOrtuError: String? = null,
    val noUrutKlgEgb: Int? = 0,
    val noUrutKlgEgbError: String? = null,
    val penglMkn: Int? = 0,
    val penglMknError: String? = null,
    val noUrutRuta: Int? = 0,
    val noUrutRutaError: String? = null,
    val kkOrKrt : String = "",
    val kkOrKrtError: String? = null,
    val namaKrt: String = "",
    val namaKrtError: String? = null,
    val genzOrtu: String = "",
    val genzOrtuError: String? = null,
    val katGenz: String = "",
    val katGenzError: String? = null,

)