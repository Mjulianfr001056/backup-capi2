package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//tiap kli buka halaman list sampel jgn akses api

@Entity(tableName = "sampel_ruta")
data class SampelRutaEntity (
    @PrimaryKey(autoGenerate = false)
    var kodeRuta: String = "",
    var noBS: String = "[Not set]",//
    var SLS: String = "[Not set]", //ganti nama var banjar
    var noSegmen: String = "[Not set]",
    var noBgFisik: String = "[Not set]",
    var noBgSensus: String = "[Not set]",
    var noUrutKlg: String = "[Not set]",//
    var noUrutRuta: Int = -1,
    var noUrutRutaEgb: Int = -1,
    var genzOrtuKeluarga: String = "[Not set]",//
    var alamat: String = "[Not set]",
    var namaKrt: String = "[Not set]",
    var genzOrtuRuta: Int = -1,
    var long: Double = -1.0,
    var lat: Double = -1.0,
    var status: String = "[Not set]" // pertimbangkan boolean
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/