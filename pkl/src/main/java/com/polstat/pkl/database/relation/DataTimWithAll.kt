package com.polstat.pkl.database.relation

import com.polstat.pkl.database.entity.DataTimEntity

data class DataTimWithAll (
    val dataTimWithMahasiswa: DataTimWithMahasiswa? = DataTimWithMahasiswa(),
    val listMahasiswaWithAll: List<MahasiswaWithAll>? = emptyList()
)