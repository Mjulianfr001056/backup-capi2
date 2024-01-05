package com.polstat.pkl.service

import com.polstat.pkl.request.LoginRequest
import com.polstat.pkl.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Interface API
interface ApiService {
//    @POST("login")
//    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("login")
    fun loginUser(@Query("nim") nim: String, @Query("password") password: String): Call<LoginResponse>
}