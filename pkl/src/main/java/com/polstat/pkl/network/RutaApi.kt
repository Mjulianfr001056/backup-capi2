package com.polstat.pkl.network

import com.polstat.pkl.model.domain.Ruta
import retrofit2.http.POST
import retrofit2.http.Query

interface RutaApi {

    // get All Ruta
    @POST("/listing/sinkronisasi-ruta")
    suspend fun rutaSynchronize(): List<Ruta>
}