package com.polstat.pkl.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class Capi63Database : RoomDatabase() {

    abstract val capi63Dao: Capi63Dao

//    companion object {
//        @Volatile
//        private var INSTANCE: Capi63Database? = null
//
//        fun getInstance(context: Context) : Capi63Database {
//            synchronized(this) {
//                return INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    Capi63Database::class.java,
//                    "capi63_db"
//                ).build().also {
//                    INSTANCE = it
//                }
//            }
//        }
//    }
}