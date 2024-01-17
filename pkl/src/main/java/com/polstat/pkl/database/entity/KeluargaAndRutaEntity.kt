package com.polstat.pkl.database.entity

import androidx.room.Entity

@Entity(primaryKeys = ["kodeKlg", "kodeRuta"])
data class KeluargaAndRutaEntity (
    val kodeKlg: String = "",
    val kodeRuta: String = ""
)