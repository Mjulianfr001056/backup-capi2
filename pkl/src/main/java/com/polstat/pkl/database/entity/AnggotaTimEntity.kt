package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//Bertanggung jawab untuk menyimpan data anggota tim
@Entity(tableName = "anggota_tim")
data class AnggotaTimEntity (
    @PrimaryKey(autoGenerate = false)
    val nim: String = "",
    val nama: String = "",
    val noTlp: String = ""
    //Tambahkan atribut lain seperlunya
)