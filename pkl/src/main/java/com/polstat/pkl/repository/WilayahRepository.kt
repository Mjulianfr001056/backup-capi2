package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface WilayahRepository {

    suspend fun insertWilayah(wilayah: Wilayah) : Flow<String>

    suspend fun updateWilayah(wilayah: Wilayah) : Flow<String>

    suspend fun getAllWilayah(): Flow<Result<List<WilayahEntity>>>

    suspend fun deleteAllWilayah() : Flow<String>

}