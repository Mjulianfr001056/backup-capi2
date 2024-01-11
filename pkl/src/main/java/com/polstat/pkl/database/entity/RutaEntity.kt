package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ruta")
data class RutaEntity (
    @PrimaryKey(autoGenerate = false)
    var kodeRuta: String,
    var noBS: String,
    var noBgFisik: Int,
    var noBgSensus: Int,
    var noSegmen: Int,
    var noUrutRtEgb: Int,
    var noUrutRuta: Int,
    var namaKrt: String,
    var alamat: String,
    var catatan: String,
    var GenzOrtu: String,
    var jmlGenz: Int,
    var lat: Double,
    var long: Double,
    var status: String
)
{
    constructor() : this("", "", 0, 0, 0, 0, 0, "", "", "", "", 0, 0.0, 0.0, "")
}