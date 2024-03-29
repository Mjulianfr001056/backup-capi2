package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.model.domain.SampelRuta
import com.polstat.pkl.model.response.SampelRutaResponse
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface SampelRutaRepository {

    suspend fun getSampelRutaFromWS(noBS: String) : Flow<Result<SampelRutaResponse>>

    suspend fun insertSampelRuta(sampelRuta: SampelRuta) : Flow<String>

    suspend fun deleteAllSampelRuta() : Flow<String>

    suspend fun getSampelRuta(noBS: String) : Flow<Result<List<SampelRutaEntity>>>

    suspend fun getAllSampelRuta() : Flow<Result<List<SampelRutaEntity>>>

    suspend fun confirmSampel(kodeRuta: String) : Flow<String>
}