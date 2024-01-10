package com.polstat.pkl.network

import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.SyncRutaResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RutaApi {

    @POST("listing/sinkronisasi-ruta")
    suspend fun sinkronisasiRuta(@Body syncRutaRequest: SyncRutaRequest) : SyncRutaResponse

}