package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.WilayahWithAll
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface WilayahRepository {

    suspend fun insertWilayah(wilayah: Wilayah, nim: String) : Flow<String>

    suspend fun updateWilayah(wilayah: Wilayah, nim: String) : Flow<String>

    suspend fun getWilayahWithRuta(noBS: String) : Flow<Result<WilayahWithRuta>>

    suspend fun getWilayahByNIM(nim: String) : Flow<Result<List<WilayahEntity>>>

    suspend fun getWilayahWithAll(noBS: String) : Flow<Result<WilayahWithAll>>

}