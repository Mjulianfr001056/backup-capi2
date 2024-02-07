package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_tim")
data class DataTimEntity (
    @PrimaryKey(autoGenerate = false)
    val idTim: String = "",
    val namaTim: String = "[Not set]",
    val passPML: String = "[Not set]",
    val namaPML: String = "[Not set]",
    val nimPML: String = "[Not set]",
    val teleponPML: String = "[Not set]"
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/