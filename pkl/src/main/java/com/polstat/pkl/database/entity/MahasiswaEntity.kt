package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mahasiswa")
data class MahasiswaEntity (
    @PrimaryKey(autoGenerate = false)
    val nim: String = "",
//    val alamat: String = "[Not set]",// tdk dipake
//    val email: String = "[Not set]", //
//    val foto: String = "[Not set]",
//    val id_tim: String = "[Not set]",
//    val isPML: Boolean = false, // akan dibuah jdi isPML
    val nama: String = "[Not set]",
    val no_hp: String = "[Not set]",
//    val password: String = "[Not set]"//
)

/** QC 23/Jan/2024
 * Pakai non-nullables semua karena udah dikasih default value
 * Secondary constructor juga ga perlu karena udah ada default value
 * Kasih default value di luar domain supaya tau kalo ada data yang belum diisi
 **/