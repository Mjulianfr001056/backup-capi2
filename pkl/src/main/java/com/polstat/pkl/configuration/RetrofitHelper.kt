package com.polstat.pkl.configuration

import com.polstat.pkl.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.100.163:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
