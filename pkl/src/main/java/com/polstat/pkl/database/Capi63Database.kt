package com.polstat.pkl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.polstat.pkl.database.converter.DateConverter
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.entity.KeluargaAndRutaEntity
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.database.entity.WilayahEntity

@Database(
    entities = [
        DataTimEntity::class,
        AnggotaTimEntity::class,
        MahasiswaEntity::class,
        WilayahEntity::class,
        KeluargaEntity::class,
        RutaEntity::class,
        SampelRutaEntity::class,
        KeluargaAndRutaEntity::class
    ],
    version = 1 // Setelah SP
)
@TypeConverters(DateConverter::class)
abstract class Capi63Database : RoomDatabase() {

    abstract val capi63Dao: Capi63Dao

}