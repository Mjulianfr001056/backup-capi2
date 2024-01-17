package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sampel_ruta")
data class SampelRutaEntity (
    @PrimaryKey(autoGenerate = false)
    var kodeRuta: String = "",
    var SLS: String = "",
    var noSegmen: Int = 0,
    var noBgFisik: String = "",
    var noBgSensus: String = "",
    var noUrutKlg: String = "",
    var noUrutRuta: Int = 0,
    var noUrutRutaEgb: Int = 0,
    var genzOrtuKeluarga: String = "",
    var alamat: String = "",
    var namaKrt: String = "",
    var genzOrtuRuta: Int = 0,
    var long: Double = 0.0,
    var lat: Double = 0.0,
    var status: String = ""
)
{
    constructor() : this("", "", 0, "", "", "", 0, 0,"", "", "",0, 0.0, 0.0, "")
}