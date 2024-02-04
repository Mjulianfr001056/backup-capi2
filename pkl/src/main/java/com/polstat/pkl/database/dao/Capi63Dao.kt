package com.polstat.pkl.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.entity.KeluargaAndRutaEntity
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.DataTimWithMahasiswa
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.database.relation.MahasiswaWithWilayah
import com.polstat.pkl.database.relation.WilayahWithKeluarga
import com.polstat.pkl.database.relation.WilayahWithRuta

@Dao
interface Capi63Dao {

    // Operasi database untuk entitas DataTim
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataTim(dataTimEntity: DataTimEntity)

    @Query("SELECT * FROM data_tim WHERE idTim = :idTim")
    suspend fun getDataTim(idTim: String) : DataTimEntity

    @Query("DELETE FROM data_tim")
    suspend fun deleteAllDataTim()

    // Operasi database untuk entitas Mahasiswa
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMahasiswa(mahasiswaEntity: MahasiswaEntity)

    @Query("DELETE FROM mahasiswa")
    suspend fun deleteAllMahasiswa()

    // Operasi database untuk entitas Wilayah

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWilayah(wilayahEntity: WilayahEntity)

    @Update
    suspend fun updateWilayah(wilayahEntity: WilayahEntity)

    @Query("DELETE FROM wilayah")
    suspend fun deleteAllWilayah()

    // Operasi database untuk entitas Keluarga

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeluarga(keluargaEntity: KeluargaEntity)

    @Update
    suspend fun updateKeluarga(keluargaEntity: KeluargaEntity)

    @Query("SELECT * FROM keluarga WHERE kodeKlg = :kodeKlg")
    suspend fun getKeluarga(kodeKlg: String) : KeluargaEntity

    @Query("SELECT * FROM keluarga WHERE status != 'delete' ORDER BY noUrutKlg DESC LIMIT 1")
    suspend fun getLastKeluarga(): KeluargaEntity

    @Query("SELECT * FROM keluarga WHERE status != 'delete' ORDER BY noUrutKlgEgb DESC LIMIT 1")
    suspend fun getLastKeluargaEgb(): KeluargaEntity

    @Query("DELETE FROM keluarga")
    suspend fun deleteAllKeluarga()

    // Operasi database untuk entitas Ruta

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuta(rutaEntity: RutaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSampelRuta(sampelRutaEntity: SampelRutaEntity)

    @Update
    suspend fun updateRuta(rutaEntity: RutaEntity)

    @Query("SELECT * FROM ruta WHERE kodeRuta = :kodeRuta")
    suspend fun getRuta(kodeRuta: String) : RutaEntity

    @Query("SELECT * FROM ruta WHERE status != 'delete' ORDER BY noUrutRuta DESC LIMIT 1")
    suspend fun getLastRuta(): RutaEntity

    @Query("DELETE FROM ruta")
    suspend fun deleteAllRuta()


    // Operasi database untuk entitas berelasi

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKelurgaAndRuta(keluargaAndRutaEntity: KeluargaAndRutaEntity)

    @Query("DELETE FROM keluargaandrutaentity")
    suspend fun deleteAllKeluargaAndRuta()

    @Transaction
    @Query("SELECT * FROM data_tim WHERE idTim = :idTim")
    suspend fun getDataTimWithMahasiswa(idTim: String) : DataTimWithMahasiswa

    @Transaction
    @Query("SELECT * FROM mahasiswa WHERE nim = :nim")
    suspend fun getMahasiswaWithWilayah(nim: String) : MahasiswaWithWilayah

    @Transaction
    @Query("SELECT * FROM wilayah WHERE noBS = :noBS")
    suspend fun getWilayahWithKeluarga(noBS: String) : WilayahWithKeluarga

    @Transaction
    @Query("SELECT * FROM wilayah WHERE noBS = :noBS")
    suspend fun getWilayahWithRuta(noBS: String) : WilayahWithRuta

    @Transaction
    @Query("SELECT * FROM wilayah WHERE nim = :nim")
    suspend fun getWilayahByNIM(nim: String) : List<WilayahEntity>

    @Transaction
    @Query("SELECT * FROM sampel_ruta WHERE noBS = :noBS")
    suspend fun getSampelRutaByNoBS(noBS: String) : List<SampelRutaEntity>

    @Query("DELETE FROM sampel_ruta")
    suspend fun deleteAllSampelRuta()

    @Transaction
    @Query("SELECT * FROM keluarga WHERE kodeKlg = :kodeKlg")
    suspend fun getKeluargaWithRuta(kodeKlg: String) : KeluargaWithRuta
}