package com.polstat.pkl.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.polstat.pkl.model.domain.Mahasiswa

@Entity(tableName = "data_tim")
data class DataTimEntity (
    @PrimaryKey(autoGenerate = false)
    val idTim: String,
    val namaTim: String,
    val passPML: String,
    val namaPML: String,
    val nimPML: String,
    val teleponPML: String
)
{
    constructor() : this("","","","","","")
}