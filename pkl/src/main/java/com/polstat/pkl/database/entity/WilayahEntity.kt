package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "wilayah")
data class WilayahEntity (
    @PrimaryKey(autoGenerate = false)
    val noBS: String = "",
    val idKab: String? = "",
    val namaKab: String? = "",
    val idKec: String? = "",
    val namaKec: String? = "",
    val idKel: String? = "",
    val namaKel: String? = "",
    val jmlRt: Int? = 0,
    val tglListing: Date? = Date(),
    val tglPeriksa: Date? = Date(),
    val catatan: String? = "",
    val status: String? = "",
    val nim: String? = ""
)
{
    constructor() : this("", "", "", "", "", "", "", 0, Date(), Date(), "", "")
}