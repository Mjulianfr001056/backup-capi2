package com.polstat.pkl.ui.state

data class IsiRutaScreenState(
    /**
     * Ini adalah state nilai tunggal untuk informasi bangunan yang merupakan atribut dari entitas keluarga
     */
    val SLS: String = "",
    val SLSError: String? = null,
    val noSegmen: String = "S000",
    val noSegmenError: String? = null,
    val noBgFisik: String = "",
    val noBgFisikError: String? = null,
    val noBgSensus: String = "",
    val noBgSensusError: String? = null,
    val jmlKlg: Int = 0,

    /**
     * Ini adalah state untuk menyimpan data beberapa keluarga
     */
    val listNoUrutKlg: List<String> = emptyList(),
    val listNoUrutKlgError: List<String> = emptyList(),
    val listNamaKK: List<String> = emptyList(),
    val listNamaKKError: List<String> = emptyList(),
    val listAlamat: List<String> = emptyList(),
    val listAlamatError: List<String> = emptyList(),
    val listIsGenzOrtu: List<Int> = emptyList(),
    val listIsGenzOrtuError: List<String> = emptyList(),
    val listNoUrutKlgEgb: List<Int> = emptyList(),
    val listNoUrutKlgEgbError: List<String> = emptyList(),
    val listPenglMkn: List<Int> = emptyList(),
    val listPenglMknError: List<String> = emptyList(),

    /**
     * Ini adalah state untuk menyimpan data beberapa ruta dalam list dua dimensi
     */
    val listNoUrutRuta: List<List<Int>> = emptyList(),
    val listNoUrutRutaError: List<List<String>> = emptyList(),
    val listKkOrKrt: List<List<String>> = emptyList(),
    val listKkOrKrtError: List<List<String>> = emptyList(),
    val listNamaKrt: List<List<String>> = emptyList(),
    val listNamaKrtError: List<List<String>> = emptyList(),
    val listGenzOrtu: List<List<Int>> = emptyList(),
    val listGenzOrtuError: List<List<String>> = emptyList(),
    val listKatGenz: List<List<Int>> = emptyList(),
    val listKatGenzError: List<List<String>> = emptyList(),
    val listKodeRuta: List<List<String>> = emptyList(),
    val listKodeRutaError: List<List<String>> = emptyList(),
    val listLong: List<List<Double>> = emptyList(),
    val listLat: List<List<Double>> = emptyList()
)