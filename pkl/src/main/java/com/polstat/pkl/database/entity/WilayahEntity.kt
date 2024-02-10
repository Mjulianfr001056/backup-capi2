package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//Digunakan untuk menyimpan data blok sensus
@Entity(tableName = "wilayah")
data class WilayahEntity (
    @PrimaryKey(autoGenerate = false)
    val idBS: String = "[not set]",
    val noBS: String = "[not set]",
    val namaKab: String = "[not set]",
    val namaKec: String = "[not set]",
    val namaKel: String = "[not set]",
    val jmlKlg: Int = -1,
    val jmlKlgEgb: Int = -1,
    val jmlRuta: Int = -1,
    val jmlRutaEgb: Int = -1,
    val tglListing: Date? = Date(),
    val tglPeriksa: Date? = Date(),
    val status: String = "[not set]"// enum/sealed class

//    val idKab: String = "[not set]", // tidak dipkai
//    val idKec: String = "[not set]", //
//    val idKel: String = "[not set]", //
//    val catatan: String = "[not set]", //
)