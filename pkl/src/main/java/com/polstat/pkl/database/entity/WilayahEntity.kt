package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "wilayah")
data class WilayahEntity (
    @PrimaryKey(autoGenerate = false)
    val noBS: String = "",
    val idKab: String = "[Not set]",
    val namaKab: String = "[Not set]",
    val idKec: String = "[Not set]",
    val namaKec: String = "[Not set]",
    val idKel: String = "[Not set]",
    val namaKel: String = "[Not set]",
    val jmlKlg: Int = -1,
    val jmlKlgEgb: Int = -1,
    val jmlRuta: Int = -1,
    val jmlRutaEgb: Int = -1,
    val tglListing: Date = Date(),
    val tglPeriksa: Date = Date(),
    val catatan: String = "[Not set]",
    val status: String = "[Not set]",
    val nim: String = "[Not set]"
)