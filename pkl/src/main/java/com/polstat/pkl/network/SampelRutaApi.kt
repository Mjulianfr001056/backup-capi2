package com.polstat.pkl.network

import com.polstat.pkl.model.domain.SampelRuta
import retrofit2.http.GET
import retrofit2.http.Path

interface SampelRutaApi {
    @GET("listing/get-sampel/{noBS}")
    suspend fun getSampel(
        @Path("noBS") noBS: String,
    ): List<SampelRuta>

}