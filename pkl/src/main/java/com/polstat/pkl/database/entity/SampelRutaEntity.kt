package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sampel_ruta")
data class SampelRutaEntity (
    @PrimaryKey(autoGenerate = false)
    var kodeRuta: String = "",
    var noBS: String? = "",
    var noBgFisik: Int? = 0,
    var noBgSensus: Int? = 0,
    var noSegmen: Int? = 0,
    var noUrutRtEgb: Int? = 0,
    var noUrutRuta: Int? = 0,
    var namaKrt: String? = "",
    var alamat: String? = "",
    var catatan: String? = "",
    var genzOrtu: String? = "",
    var jmlGenz: Int? = 0,
    var lat: Double? = 0.0,
    var long: Double? = 0.0,
)
{
    constructor() : this("", "", 0, 0, 0, 0, 0, "", "", "", "", 0, 0.0, 0.0)
}