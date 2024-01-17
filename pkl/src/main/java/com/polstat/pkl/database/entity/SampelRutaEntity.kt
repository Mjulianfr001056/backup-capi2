package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sampel_ruta")
data class SampelRutaEntity (
    @PrimaryKey(autoGenerate = false)
    var kodeRuta: String = "",
    val noUrutRuta: Int? = 0,
    val kkOrKrt: String? = "",
    val namaKrt: String? = "",
    val genzOrtu: String? = "",
    val katGenz: String? = "",
    val long: Double? = 0.0,
    val lat: Double? = 0.0,
    val catatan: String? = "",
    val noBS: String? = "",
    var status: String? = ""
)
{
    constructor() : this("", 0, "", "", "", "", 0.0, 0.0, "", "", "")
}