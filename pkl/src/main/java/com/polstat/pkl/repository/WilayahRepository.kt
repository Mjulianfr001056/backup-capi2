package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.model.domain.Mahasiswa
import com.polstat.pkl.model.domain.Wilayah
import kotlinx.coroutines.flow.Flow
import com.polstat.pkl.utils.Result

interface WilayahRepository {

    suspend fun insertWilayah(wilayah: Wilayah, nim: String) : Flow<String>

    suspend fun getWilayahWithRuta(noBS: String) : Flow<Result<WilayahWithRuta>>

    suspend fun getWilayahByNIM(nim: String) : Flow<Result<List<WilayahEntity>>>

}