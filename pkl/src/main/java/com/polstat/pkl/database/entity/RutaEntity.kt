package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ruta")
data class RutaEntity (
    @PrimaryKey(autoGenerate = false)
    var kodeRuta: String = "",
    var noUrutRuta: Int = -1,
    var noUrutEgb: Int = -1,
    var kkOrKrt: String = "[Not set]",
    var namaKrt: String = "[Not set]",
    var genzOrtu: Int = -1,
    var katGenz: Int = -1,
    var long: Double = -1.0,
    var lat: Double = -1.0,
    var catatan: String = "[Not set]",
    var noBS: String = "[Not set]",
    var status: String = "[Not set]"
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/