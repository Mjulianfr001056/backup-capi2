package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ruta")
data class RutaEntity (
    @PrimaryKey(autoGenerate = false)
    var kodeRuta: String = "[not set]",
    var noUrutRuta: String = "[not set]",
    var noUrutEgb: Int? = -1,
    var kkOrKrt: String = "[not set]",
    var namaKrt: String = "[not set]",
    var jmlGenzAnak: Int = -1,
    var jmlGenzDewasa: Int = -1,
    var katGenz: Int = -1,
    var long: Double = -1.0,
    var lat: Double = -1.0,
    var catatan: String = "[not set]",
    var idBS: String = "[not set]",
    var nimPencacah: String = "[not set]",
    var status: String = "[not set]"
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/