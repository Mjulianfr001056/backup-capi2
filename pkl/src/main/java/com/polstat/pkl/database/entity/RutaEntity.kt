package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ruta")
data class RutaEntity (
    @PrimaryKey(autoGenerate = false)
    var kodeRuta: String = "",
    var noUrutRuta: Int? = 0,
    var kkOrKrt: String? = "",
    var namaKrt: String? = "",
    var genzOrtu: String? = "",
    var katGenz: String? = "",
    var long: Double? = 0.0,
    var lat: Double? = 0.0,
    var catatan: String? = "",
    var noBS: String? = "",
    var status: String? = ""
)
{
    constructor() : this("", 0, "", "", "", "", 0.0, 0.0, "", "")
}