package com.polstat.pkl.ui.state

data class IsiRutaScreenState(
    val noSegmen: String = "001",
    val noSegmenError: String? = null,
    val noBgFisik: String = "001",
    val noBgFisikError: String? = null,
    val noBgSensus: String = "001",
    val noBgSensusError: String? = null,
    val noUrutRuta: String = "001",
    val noUrutRutaError: String? = null,
    val namaKrt: String = "",
    val namaKrtError: String? = null,
    val alamat: String = "",
    val alamatError: String? = null,
    val isGenzOrtu: String = "Tidak",
    val isGenzOrtuError: String? = null,
    val jmlGenz: String = "000",
    val jmlGenzError: String? = null,
    val noUrutRtEgb: String = "001",
    val noUrutRtEgbError: String? = null,
    val catatan: String = "",
    val catatanError: String? = null
)