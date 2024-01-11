package com.polstat.pkl.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.DataTimWithMahasiswa
import com.polstat.pkl.database.relation.MahasiswaWithWilayah
import com.polstat.pkl.database.relation.WilayahWithRuta

@Dao
interface Capi63Dao {

    // Operasi database untuk entitas DataTim

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataTim(dataTimEntity: DataTimEntity)

    @Query("SELECT * FROM data_tim WHERE idTim = :idTim")
    suspend fun getDataTim(idTim: String) : DataTimEntity

    // Operasi database untuk entitas Mahasiswa

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMahasiswa(mahasiswaEntity: MahasiswaEntity)

    // Operasi database untuk entitas Wilayah

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWilayah(wilayahEntity: WilayahEntity)

    // Operasi database untuk entitas Ruta

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuta(rutaEntity: RutaEntity)

    @Update
    suspend fun updateRuta(rutaEntity: RutaEntity)


    // Operasi database untuk entitas berelasi

    @Transaction
    @Query("SELECT * FROM data_tim WHERE idTim = :idTim")
    suspend fun getDataTimWithMahasiswa(idTim: String) : DataTimWithMahasiswa

    @Transaction
    @Query("SELECT * FROM mahasiswa WHERE nim = :nim")
    suspend fun getMahasiswaWithWilayah(nim: String) : MahasiswaWithWilayah

    @Transaction
    @Query("SELECT * FROM wilayah WHERE noBS = :noBS")
    suspend fun getWilayahWithRuta(noBS: String) : WilayahWithRuta

}