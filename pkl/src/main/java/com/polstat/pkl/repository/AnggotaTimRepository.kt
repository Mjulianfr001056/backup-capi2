package com.polstat.pkl.repository

import com.polstat.pkl.model.domain.AnggotaTim
import kotlinx.coroutines.flow.Flow

interface AnggotaTimRepository {
    suspend fun insert(anggota: AnggotaTim) : Flow<String>
}