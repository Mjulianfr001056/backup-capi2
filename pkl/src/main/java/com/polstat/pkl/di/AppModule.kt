package com.polstat.pkl.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.network.SampelRutaApi
import com.polstat.pkl.repository.DataTimRepository
import com.polstat.pkl.repository.DataTimRepositoryImpl
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.LocalRutaRepositoryImpl
import com.polstat.pkl.repository.MahasiswaRepository
import com.polstat.pkl.repository.MahasiswaRepositoryImpl
import com.polstat.pkl.repository.SampelRutaRepository
import com.polstat.pkl.repository.SampelRutaRepositoryImpl
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.SessionRepositoryImpl
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.repository.WilayahRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        private const val TAG = "CAPI63_APP_MODULE"
    }

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

    @Provides
    @Singleton
    fun provideCapi63Database(@ApplicationContext context: Context) : Capi63Database {
        Log.d(TAG, "Database initialized successfully")
        return Room.databaseBuilder(
            context,
            Capi63Database::class.java,
            "capi63_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCapi63Dao(capi63Database: Capi63Database) : Capi63Dao {
        return capi63Database.capi63Dao
    }

    @Provides
    @Singleton
    fun provideDataTimRepository(capi63Database: Capi63Database) : DataTimRepository {
        return DataTimRepositoryImpl(capi63Database)
    }

    @Provides
    @Singleton
    fun provideMahasiswaRepository(capi63Database: Capi63Database) : MahasiswaRepository {
        return MahasiswaRepositoryImpl(capi63Database)
    }

    @Provides
    @Singleton
    fun provideWilayahRepository(capi63Database: Capi63Database) : WilayahRepository {
        return WilayahRepositoryImpl(capi63Database)
    }

    @Provides
    @Singleton
    fun provideLocalRutaRepository(capi63Database: Capi63Database) : LocalRutaRepository {
        return LocalRutaRepositoryImpl(capi63Database)
    }

}