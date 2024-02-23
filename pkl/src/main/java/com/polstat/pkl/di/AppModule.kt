package com.polstat.pkl.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.google.android.gms.location.LocationServices
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.repository.AnggotaTimRepository
import com.polstat.pkl.repository.AnggotaTimRepositoryImpl
import com.polstat.pkl.repository.DataTimRepository
import com.polstat.pkl.repository.DataTimRepositoryImpl
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.KeluargaRepositoryImpl
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.repository.LocalRutaRepositoryImpl
import com.polstat.pkl.repository.MahasiswaRepository
import com.polstat.pkl.repository.MahasiswaRepositoryImpl
import com.polstat.pkl.repository.SessionRepository
import com.polstat.pkl.repository.SessionRepositoryImpl
import com.polstat.pkl.repository.WilayahRepository
import com.polstat.pkl.repository.WilayahRepositoryImpl
import com.polstat.pkl.utils.location.ILocationService
import com.polstat.pkl.utils.location.LocationService
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

    @Singleton
    @Provides
    fun provideLocationClient(
        @ApplicationContext context: Context
    ): ILocationService = LocationService(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )

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
    fun provideAnggotaTimRepository(capi63Dao: Capi63Dao): AnggotaTimRepository {
        return AnggotaTimRepositoryImpl(capi63Dao)
    }

    @Provides
    @Singleton
    fun provideDataTimRepository(capi63Dao: Capi63Dao) : DataTimRepository {
        return DataTimRepositoryImpl(capi63Dao)
    }

    @Provides
    @Singleton
    fun provideMahasiswaRepository(capi63Database: Capi63Database) : MahasiswaRepository {
        return MahasiswaRepositoryImpl(capi63Database)
    }

    @Provides
    @Singleton
    fun provideWilayahRepository(capi63Dao: Capi63Dao) : WilayahRepository {
        return WilayahRepositoryImpl(capi63Dao)
    }

    @Provides
    @Singleton
    fun provideLocalRutaRepository(capi63Dao: Capi63Dao) : LocalRutaRepository {
        return LocalRutaRepositoryImpl(capi63Dao)
    }

    @Provides
    @Singleton
    fun provideKeluargaRepository(capi63Dao: Capi63Dao) : KeluargaRepository {
        return KeluargaRepositoryImpl(capi63Dao)
    }
}