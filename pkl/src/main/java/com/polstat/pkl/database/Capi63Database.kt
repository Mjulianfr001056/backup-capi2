package com.polstat.pkl.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.polstat.pkl.database.converter.DateConverter
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.entity.WilayahEntity

@Database(
    entities = [
        DataTimEntity::class,
        MahasiswaEntity::class,
        WilayahEntity::class,
        RutaEntity::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class Capi63Database : RoomDatabase() {

    abstract val capi63Dao: Capi63Dao

}