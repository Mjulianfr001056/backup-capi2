package com.polstat.pkl.di

import android.content.SharedPreferences
import com.polstat.pkl.auth.network.AuthApi
import com.polstat.pkl.auth.repository.AuthRepository
import com.polstat.pkl.auth.repository.AuthRepositoryImpl
import com.polstat.pkl.auth.repository.SessionRepository
import com.polstat.pkl.auth.repository.SessionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    val BASE_URL = "https://4f37-103-162-62-54.ngrok-free.app"

    @Provides
    @Singleton
    fun OkHttpClient(): OkHttpClient {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi, sessionRepository: SessionRepository) : AuthRepository {
        return AuthRepositoryImpl(authApi, sessionRepository)
    }


}