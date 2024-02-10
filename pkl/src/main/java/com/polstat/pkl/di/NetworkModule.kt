package com.polstat.pkl.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.network.AuthApi
import com.polstat.pkl.network.LocationApi
import com.polstat.pkl.network.RutaApi
import com.polstat.pkl.network.SampelRutaApi
import com.polstat.pkl.repository.AuthRepository
import com.polstat.pkl.repository.AuthRepositoryImpl
import com.polstat.pkl.repository.LocationRepository
import com.polstat.pkl.repository.LocationRepositoryImpl
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
//    private val BASE_URL = "https://b64c-111-94-54-212.ngrok-free.app/"
    private val BASE_URL = "https://capi.pkl63.stis.ac.id/"

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
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
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
    fun provideLocationApi(retrofit: Retrofit) : LocationApi {
        return retrofit.create(LocationApi::class.java)
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
    fun provideSampelRutaRepository(sampelRutaApi: SampelRutaApi, capi63Dao: Capi63Dao) : SampelRutaRepository {
        return SampelRutaRepositoryImpl(sampelRutaApi, capi63Dao)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(locationApi: LocationApi) : LocationRepository {
        return LocationRepositoryImpl(locationApi)
    }
}