package com.polstat.pkl.repository

import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.FinalisasiBSResponse
import com.polstat.pkl.model.response.SyncRutaResponse
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface RemoteRutaRepository {

    suspend fun sinkronisasiRuta(syncRutaRequest: SyncRutaRequest) : Flow<Result<SyncRutaResponse>>

    suspend fun generateRuta(noBS: String) : Flow<String>

    suspend fun finalisasiBS(noBS: String) : Flow<Result<FinalisasiBSResponse>>

}