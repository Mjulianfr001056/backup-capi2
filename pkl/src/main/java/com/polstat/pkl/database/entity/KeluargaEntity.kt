package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keluarga")
data class KeluargaEntity (
    @PrimaryKey(autoGenerate = false)
    val kodeKlg: String = "",
    val SLS: String = "[Not set]",
    val noBgFisik: String = "[not set]",
    val noBgSensus: String = "[not set]",
    val noSegmen: String = "S000",
    val noUrutKlg: Int = -1,
    val noUrutKlgEgb: Int = -1,
    val namaKK: String = "[Not set]",
    val alamat: String = "[Not set]",
    val isGenzOrtu: Int = -1,
    val penglMkn: Int = -1,
    val noBS: String = "[Not set]",
    val status: String = "[Not set]"
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/