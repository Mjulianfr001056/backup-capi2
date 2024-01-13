package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mahasiswa")
data class MahasiswaEntity (
    @PrimaryKey(autoGenerate = false)
    val nim: String = "",
    val nama: String? = "",
    val isKoor: Boolean? = false,
    val password: String? = "",
    val alamat: String? = "",
    val email: String? = "",
    val foto: String? = "",
    val id_tim: String? = "",
    val no_hp: String? = ""
)
{
    constructor() : this("","",false,"","","","","","")
}