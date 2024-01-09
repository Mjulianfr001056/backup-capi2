package com.polstat.pkl.network

import retrofit2.http.POST

interface RutaApi {
    @POST("/listing/sinkronisasi-ruta")
    suspend fun rutaSynchronize()
}