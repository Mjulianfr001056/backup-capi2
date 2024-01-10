package com.polstat.pkl.di

import com.polstat.pkl.network.RutaApi
import com.polstat.pkl.repository.RutaRepository
import com.polstat.pkl.repository.RutaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RutaModule {

    @Provides
    @Singleton
    fun provideRutaApi(retrofit: Retrofit): RutaApi {
        return retrofit.create(RutaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRutaRepository(rutaApi: RutaApi): RutaRepository {
        return RutaRepositoryImpl(rutaApi)
    }
}