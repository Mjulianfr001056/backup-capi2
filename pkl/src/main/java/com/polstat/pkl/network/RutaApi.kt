package com.polstat.pkl.network

import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.FinalisasiBSResponse
import com.polstat.pkl.model.response.SyncRutaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RutaApi {

    @POST("listing/sinkronisasi-ruta")
    suspend fun sinkronisasiRuta(@Body syncRutaRequest: SyncRutaRequest) : SyncRutaResponse

    @GET("listing/generate-sampel/{noBS}")
    suspend fun generateSampel(
        @Path("noBS") noBS: String
    )

    @GET("listing/finalisasi-bs/{noBS}")
    suspend fun finalisasiBS(
        @Path("noBS")
        noBS: String
    ) : FinalisasiBSResponse

}