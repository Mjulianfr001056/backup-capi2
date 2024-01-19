package com.polstat.pkl.network

import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.SyncRutaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RutaApi {

    @POST("listing/sinkronisasi-ruta")
    suspend fun sinkronisasiRuta(@Body syncRutaRequest: SyncRutaRequest) : SyncRutaResponse

    @GET("generate-sampel")
    suspend fun generateSampel(
        @Query("noBS") noBS: String
    )

}