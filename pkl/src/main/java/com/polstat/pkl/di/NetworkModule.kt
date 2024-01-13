package com.polstat.pkl.di

import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.network.AuthApi
import com.polstat.pkl.network.RutaApi
import com.polstat.pkl.network.SampelRutaApi
import com.polstat.pkl.repository.AuthRepository
import com.polstat.pkl.repository.AuthRepositoryImpl
import com.polstat.pkl.repository.RemoteRutaRepository
import com.polstat.pkl.repository.RemoteRutaRepositoryImpl
import com.polstat.pkl.repository.SampelRutaRepository
import com.polstat.pkl.repository.SampelRutaRepositoryImpl
import com.polstat.pkl.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    val BASE_URL = "https://c4b4-103-162-62-54.ngrok-free.app/"

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
    fun provideRutaApi(retrofit: Retrofit) : RutaApi {
        return retrofit.create(RutaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSampelRutaApi(retrofit: Retrofit) : SampelRutaApi {
        return retrofit.create(SampelRutaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi, sessionRepository: SessionRepository) : AuthRepository {
        return AuthRepositoryImpl(authApi, sessionRepository)
    }

    @Provides
    @Singleton
    fun provideRemoteRutaRepository(rutaApi: RutaApi) : RemoteRutaRepository {
        return RemoteRutaRepositoryImpl(rutaApi)
    }

    @Provides
    @Singleton
    fun provideSampelRutaRepository(sampelRutaApi: SampelRutaApi, capi63Database: Capi63Database) : SampelRutaRepository {
        return SampelRutaRepositoryImpl(sampelRutaApi, capi63Database)
    }}