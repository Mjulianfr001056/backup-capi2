package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mahasiswa")
data class MahasiswaEntity (
    @PrimaryKey(autoGenerate = false)
    val nim: String = "",
    val alamat: String? = "",
    val email: String? = "",
    val foto: String? = "",
    val idTim: String? = "",
    val isKoor: Boolean? = false,
    val nama: String? = "",
    val noHp: String? = "",
    val password: String? = "",
)
{
    constructor() : this("", "", "", "", "", false, "", "", "")
}