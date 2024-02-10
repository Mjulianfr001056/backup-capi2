package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.model.domain.AnggotaTim
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface AnggotaTimRepository {
    suspend fun insertAnggotaTim(anggota: AnggotaTim) : Flow<String>
    suspend fun getAllAnggotaTim() : Flow<Result<List<AnggotaTimEntity>>>
    suspend fun deleteAllAnggotaTim() : Flow<String>
}