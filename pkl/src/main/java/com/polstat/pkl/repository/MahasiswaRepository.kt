package com.polstat.pkl.repository

import com.polstat.pkl.model.domain.Mahasiswa
import kotlinx.coroutines.flow.Flow

interface MahasiswaRepository {
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa): Flow<String>

    suspend fun deleteAllMahasiswa() : Flow<String>
}