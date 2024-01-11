package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "wilayah")
data class WilayahEntity (
    @PrimaryKey(autoGenerate = false)
    val noBS: String,
    val idKab: String,
    val idKec: String,
    val idKel: String,
    val namaKab: String,
    val namaKec: String,
    val namaKel: String,
    val catatan: String,
    val jmlGenZ: Int,
    val jmlRt: Int,
    val jmlRtGenz: Int,
    val status: String,
    val tglListing: Date,
    val tglPeriksa: Date,
    val nim: String
)
{
    constructor() : this("","","","","","","","",0,0,0,"",Date(),Date(),"")
}