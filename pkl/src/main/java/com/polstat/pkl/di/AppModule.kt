package com.polstat.pkl.di

import android.content.Context
import android.content.SharedPreferences
import com.polstat.pkl.auth.repository.SessionRepository
import com.polstat.pkl.auth.repository.SessionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("CAPI63", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(sharedPreferences: SharedPreferences): SessionRepository {
        return SessionRepositoryImpl(sharedPreferences)
    }

}