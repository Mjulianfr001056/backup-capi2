package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.database.relation.RutaWithKeluarga
import com.polstat.pkl.database.relation.WilayahWithAll
import com.polstat.pkl.database.relation.WilayahWithKeluarga
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.mapper.toWilayahEntity
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WilayahRepositoryImpl @Inject constructor(
    private val capi63Database: Capi63Database
) : WilayahRepository {

    companion object {
        private const val TAG = "CAPI63_WILAYAHREPOIMPL"
    }

    override suspend fun insertWilayah(
        wilayah: Wilayah
    ): Flow<String> {
        return flow {
            try {
                capi63Database.capi63Dao.insertWilayah(wilayah.toWilayahEntity())
                val message = "Berhasil menambahkan wilayah!"
                Log.d(TAG, "insertWilayah: $message $wilayah")
                emit(message)
            } catch (e: SQLiteConstraintException) {
                val message = "Gagal menambahkan wilayah! Constraint pelanggaran: ${e.message}"
                Log.d(TAG, "insertWilayah: $message")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan wilayah! Kesalahan umum: ${e.message}"
                Log.d(TAG, "insertWilayah: $message")
                emit(message)
            }
        }
    }

    override suspend fun updateWilayah(wilayah: Wilayah): Flow<String> {
        return  flow {
            try {
                capi63Database.capi63Dao.updateWilayah(wilayah.toWilayahEntity())
                val message = "Berhasil mengupdate wilayah!"
                Log.d(TAG, "updateWilayah: $message")
            } catch (e: Exception) {
                val message = "Gagal mengupdate wilayah!"
                Log.d(TAG, "updateWilayah: $message (${e.message})")
            }
        }
    }

    override suspend fun deleteAllWilayah(): Flow<String> {
        return  flow {
            try {
                capi63Database.capi63Dao.deleteAllWilayah()
                val message = "Berhasil menghapus seluruh wilayah!"
                Log.d(TAG, "deleteAllWilayah: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh wilayah!"
                Log.d(TAG, "deleteAllWilayah: $message (${e.message})")
            }
        }
    }

    override suspend fun getWilayahWithRuta(
        noBS: String
    ): Flow<Result<WilayahWithRuta>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val wilayahWithRuta = capi63Database.capi63Dao.getWilayahWithRuta(noBS)
                Log.d(TAG, "Berhasil getWilayahWithRuta: $wilayahWithRuta")
                emit(Result.Success(wilayahWithRuta))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getWilayahWithRuta: ${e.message}")
                emit(Result.Error(null, "Error fetching WilayahWithRuta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getWilayahWithKeluarga(
        noBS: String
    ): Flow<Result<WilayahWithKeluarga>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val wilayahWithKeluarga = capi63Database.capi63Dao.getWilayahWithKeluarga(noBS)
                Log.d(TAG, "Berhasil getWilayahWithKeluarga: $wilayahWithKeluarga")
                emit(Result.Success(wilayahWithKeluarga))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getWilayahWithKeluarga: ${e.message}")
                emit(Result.Error(null, "Error fetching getWilayahWithKeluarga: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getWilayahByNIM(
        nim: String
    ): Flow<Result<List<WilayahEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val wilayahList = capi63Database.capi63Dao.getWilayahByNIM(nim)
                Log.d(TAG, "Berhasil getWilayahByNIM: $wilayahList")
                emit(Result.Success(wilayahList))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getWilayahByNIM: ${e.message}")
                emit(Result.Error(null, "Error fetching WilayahByNIM: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getWilayahWithAll(
        noBS: String
    ): Flow<Result<WilayahWithAll>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val wilayahWithKeluarga = capi63Database.capi63Dao.getWilayahWithKeluarga(noBS)
                val listKeluargaWithRuta = wilayahWithKeluarga.listKeluarga?.map { keluarga ->
                    val ruta = capi63Database.capi63Dao.getKeluargaWithRuta(keluarga.kodeKlg)
                    KeluargaWithRuta(keluarga, ruta.listRuta)
                }
                val wilayahWithRuta = capi63Database.capi63Dao.getWilayahWithRuta(noBS)
                val listRutaWithKeluarga = wilayahWithRuta.listRuta?.map { ruta ->
                    val keluarga = capi63Database.capi63Dao.getRutaWithKeluarga(ruta.kodeRuta)
                    RutaWithKeluarga(ruta, keluarga.listKeluarga)
                }
                val wilayahWithAll = WilayahWithAll(
                    wilayahWithKeluarga = wilayahWithKeluarga,
                    wilayahWithRuta = wilayahWithRuta,
                    listKeluargaWithRuta = listKeluargaWithRuta,
                    listRutaWithKeluarga = listRutaWithKeluarga
                )
                Log.d(TAG, "Berhasil getWilayahWithAll: $wilayahWithAll")
                emit(Result.Success(wilayahWithAll))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getWilayahWithAll: ${e.message}")
                emit(Result.Error(null, "Error fetching WilayahWithAll: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

}