package com.polstat.pkl.network

import com.polstat.pkl.model.response.AuthResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthApi {
    @GET("login")
    suspend fun login(
        @Query("nim") nim: String,
        @Query("password") password: String
    ): AuthResponse

}