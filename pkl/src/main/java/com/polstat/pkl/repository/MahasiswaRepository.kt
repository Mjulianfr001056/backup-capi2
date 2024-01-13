package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.MahasiswaWithAll
import com.polstat.pkl.database.relation.MahasiswaWithWilayah
import com.polstat.pkl.model.domain.Mahasiswa
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface MahasiswaRepository {
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa): Flow<String>

    suspend fun getMahasiswaWithWilayah(nim: String): Flow<Result<MahasiswaWithWilayah>>

    suspend fun getMahasiswaWithAll(nim: String) : Flow<Result<MahasiswaWithAll>>
}