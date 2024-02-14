package com.polstat.pkl.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.entity.KeluargaAndRutaEntity
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.MahasiswaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.database.relation.RutaWithKeluarga

@Dao
interface Capi63Dao {

    // Operasi database untuk entitas DataTim
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataTim(dataTimEntity: DataTimEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnggotaTim(anggotaTimEntity: AnggotaTimEntity)

    @Query("SELECT * FROM anggota_tim")
    suspend fun getAllAnggotaTim() : List<AnggotaTimEntity>

    @Query("DELETE FROM anggota_tim")
    suspend fun deleteAllAnggotaTim()

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

    @Query("SELECT * FROM wilayah WHERE idBS = :idBS")
    suspend fun getWilayah(idBS: String) : WilayahEntity

    @Query("SELECT * FROM wilayah")
    suspend fun getAllWilayah(): List<WilayahEntity>

    @Query("DELETE FROM wilayah")
    suspend fun deleteAllWilayah()

    // Operasi database untuk entitas Keluarga

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeluarga(keluargaEntity: KeluargaEntity)

    @Update
    suspend fun updateKeluarga(keluargaEntity: KeluargaEntity)

    @Query("SELECT * FROM keluarga WHERE kodeKlg = :kodeKlg AND status != 'delete'")
    suspend fun getKeluarga(kodeKlg: String) : KeluargaEntity

    @Query("SELECT * FROM keluarga WHERE idBS = :idBS ORDER BY CAST(noUrutKlg AS INTEGER) ASC")
    suspend fun getAllKeluargaByWilayah(idBS: String) : List<KeluargaEntity>

    @Transaction
    @Query("SELECT * FROM keluarga INNER JOIN KeluargaAndRutaEntity ON keluarga.kodeKlg = KeluargaAndRutaEntity.kodeKlg WHERE KeluargaAndRutaEntity.kodeRuta = :kodeRuta ORDER BY CAST(keluarga.noUrutKlg AS INTEGER) ASC")
    suspend fun getAllKeluargaByRuta(kodeRuta: String): List<KeluargaEntity>

    @Query("SELECT * FROM keluarga WHERE status != 'delete' ORDER BY CAST(noUrutKlg AS INTEGER) DESC LIMIT 1")
    suspend fun getLastKeluarga(): KeluargaEntity

    @Query("SELECT * FROM keluarga WHERE status != 'delete' ORDER BY CAST(noUrutKlgEgb AS INTEGER) DESC LIMIT 1")
    suspend fun getLastKeluargaEgb(): KeluargaEntity

    @Query("DELETE FROM keluarga")
    suspend fun deleteAllKeluarga()

    @Query("DELETE FROM keluarga WHERE idBS = :idBS")
    suspend fun deleteAllKeluargaByWilayah(idBS: String)

    // Operasi database untuk entitas Ruta

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuta(rutaEntity: RutaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSampelRuta(sampelRutaEntity: SampelRutaEntity)

    @Update
    suspend fun updateRuta(rutaEntity: RutaEntity)

    @Query("SELECT * FROM ruta WHERE kodeRuta = :kodeRuta AND status != 'delete'")
    suspend fun getRuta(kodeRuta: String) : RutaEntity

    @Query("SELECT * FROM ruta WHERE idBS = :idBS ORDER BY CAST(noUrutRuta AS INTEGER) ASC")
    suspend fun getAllRutaByWilayah(idBS: String) : List<RutaEntity>

    @Query("SELECT * FROM ruta WHERE status != 'delete' ORDER BY CAST(noUrutRuta AS INTEGER) DESC LIMIT 1")
    suspend fun getLastRuta(): RutaEntity

    @Query("DELETE FROM ruta")
    suspend fun deleteAllRuta()

    @Query("DELETE FROM ruta WHERE idBS = :idBS")
    suspend fun deleteAllRutaByWilayah(idBS: String)

    @Transaction
    @Query("SELECT * FROM ruta INNER JOIN KeluargaAndRutaEntity ON ruta.kodeRuta = KeluargaAndRutaEntity.kodeRuta WHERE KeluargaAndRutaEntity.kodeKlg = :kodeKlg ORDER BY ruta.noUrutRuta ASC")
    suspend fun getAllRutaByKeluarga(kodeKlg: String): List<RutaEntity>


    // Operasi database untuk entitas berelasi

    @Transaction
    @Query("SELECT * FROM keluarga WHERE idBS = :idBS ORDER BY CAST(keluarga.noUrutKlg AS INTEGER) ASC")
    fun getListKeluargaWithRuta(idBS: String): List<KeluargaWithRuta>

    @Transaction
    @Query("SELECT * FROM ruta WHERE idBS = :idBS ORDER BY CAST(ruta.noUrutRuta AS INTEGER) ASC")
    fun getListRutaWithKeluarga(idBS: String): List<RutaWithKeluarga>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKelurgaAndRuta(keluargaAndRutaEntity: KeluargaAndRutaEntity)

    @Query("DELETE FROM KeluargaAndRutaEntity WHERE kodeKlg IN (SELECT kodeKlg FROM keluarga WHERE idBS = :idBS) AND kodeRuta IN (SELECT kodeRuta FROM ruta WHERE idBS = :idBS)")
    suspend fun deleteAllKeluargaAndRutaByWilayah(idBS: String)

    @Transaction
    suspend fun deleteAllKeluargaRutaAndRelationByWilayah(idBS: String) {
        deleteAllKeluargaByWilayah(idBS)
        deleteAllRutaByWilayah(idBS)
        deleteAllKeluargaAndRutaByWilayah(idBS)
    }

    @Query("DELETE FROM keluargaandrutaentity")
    suspend fun deleteAllKeluargaAndRuta()

    @Transaction
    @Query("SELECT * FROM sampel_ruta WHERE idBS = :idBS")
    suspend fun getSampelRutaByNoBS(idBS: String) : List<SampelRutaEntity>

    @Query("SELECT * FROM sampel_ruta")
    suspend fun getAllSampelRuta() : List<SampelRutaEntity>
    @Query("DELETE FROM sampel_ruta")
    suspend fun deleteAllSampelRuta()
}