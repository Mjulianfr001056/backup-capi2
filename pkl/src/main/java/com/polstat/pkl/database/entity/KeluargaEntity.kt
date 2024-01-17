package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keluarga")
data class KeluargaEntity (
    @PrimaryKey(autoGenerate = false)
    val kodeKlg: String = "",
    val SLS: String? = "",
    val noBgFisik: Int? = 0,
    val noBgSensus: Int? = 0,
    val noSegmen: String? = "",
    val noUrutKlg: Int? = 0,
    val noUrutKlgEgb: Int? = 0,
    val namaKK: String? = "",
    val alamat: String? = "",
    val isGenzOrtu: Int? = 0,
    val penglMkn: Int? = 0,
    val noBS: String? = ""
)
{
    constructor() : this("", "", 0, 0, "", 0, 0, "", "", 0, 0, "")
}