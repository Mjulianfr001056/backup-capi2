package com.polstat.pkl.ui.state


data class Message(
    val warning: String? = null,
    val error: String? = null
)
data class IsiRutaScreenState(
    /**
     * Ini adalah state nilai tunggal untuk informasi bangunan yang merupakan atribut dari entitas keluarga
     */
    val SLS: String = "",
    val SLSMsg: Message = Message(),
    val noSegmen: String = "S000",
    val noSegmenMsg: Message = Message(),
    val noBgFisik: String = "",
    val noBgFisikMsg: Message = Message(),
    val noBgSensus: String = "",
    val noBgSensusMsg: Message = Message(),
    val jmlKlg: Int = 0,

    /**
     * Ini adalah state untuk menyimpan data beberapa keluarga
     */
    val listNoUrutKlg: List<String> = emptyList(),
    val listNoUrutKlgMsg: List<Message> = emptyList(),
    val listNamaKK: List<String> = emptyList(),
    val listNamaKKMsg: List<Message> = emptyList(),
    val listAlamat: List<String> = emptyList(),
    val listAlamatMsg: List<Message> = emptyList(),
    val listIsGenzOrtu: List<Int> = emptyList(),
    val listIsGenzOrtuMsg: List<Message> = emptyList(),
    val listNoUrutKlgEgb: List<Int> = emptyList(),
    val listNoUrutKlgEgbMsg: List<Message> = emptyList(),
    val listPenglMkn: List<Int> = emptyList(),
    val listPenglMknMsg: List<Message> = emptyList(),

    /**
     * Ini adalah state untuk menyimpan data beberapa ruta dalam list dua dimensi
     */
    val listNoUrutRuta: List<List<String>> = emptyList(),
    val listNoUrutRutaMsg: List<List<Message>> = emptyList(),
    val listKkOrKrt: List<List<String>> = emptyList(),
    val listKkOrKrtMsg: List<List<Message>> = emptyList(),
    val listNamaKrt: List<List<String>> = emptyList(),
    val listNamaKrtMsg: List<List<Message>> = emptyList(),
    val listJmlGenzAnak: List<List<Int>> = emptyList(),
    val listJmlGenzAnakMsg: List<List<Message>> = emptyList(),
    val listJmlGenzDewasa: List<List<Int>> = emptyList(),
    val listJmlGenzDewasaMsg: List<List<Message>> = emptyList(),
    val listKatGenz: List<List<Int>> = emptyList(),
    val listKatGenzMsg: List<List<Message>> = emptyList(),
    val listLat: List<List<Double>> = emptyList(),
    val listLong: List<List<Double>> = emptyList(),
    val listCatatan: List<List<String>> = emptyList()
)