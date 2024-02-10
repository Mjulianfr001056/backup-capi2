package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.mapper.toWilayahEntity
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WilayahRepositoryImpl @Inject constructor(
    private val capi63Dao: Capi63Dao
) : WilayahRepository {

    companion object {
        private const val TAG = "CAPI63_WILAYAHREPOIMPL"
    }

    override suspend fun insertWilayah(
        wilayah: Wilayah
    ): Flow<String> {
        return flow {
            try {
                capi63Dao.insertWilayah(wilayah.toWilayahEntity())
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

    override suspend fun updateWilayah(wilayahEntity: WilayahEntity): Flow<String> {
        return  flow {
            try {
                capi63Dao.updateWilayah(wilayahEntity)
                val message = "Berhasil mengupdate wilayah!"
                Log.d(TAG, "updateWilayah: $message")
            } catch (e: Exception) {
                val message = "Gagal mengupdate wilayah!"
                Log.d(TAG, "updateWilayah: $message (${e.message})")
            }
        }
    }

    override suspend fun getWilayah(idBS: String): Flow<Result<WilayahEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val wilayah = capi63Dao.getWilayah(idBS)
                Log.d(TAG, "Berhasil getWilayah: $wilayah")
                emit(Result.Success(wilayah))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getWilayah: ${e.message}")
                emit(Result.Error(null, "Error Get Wilayah: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getAllWilayah(): Flow<Result<List<WilayahEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listWilayah = capi63Dao.getAllWilayah()
                Log.d(TAG, "Berhasil getAllWilayah: $listWilayah")
                emit(Result.Success(listWilayah))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getAllWilayah: ${e.message}")
                emit(Result.Error(null, "Error Get All Wilayah: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun deleteAllWilayah(): Flow<String> {
        return  flow {
            try {
                capi63Dao.deleteAllWilayah()
                val message = "Berhasil menghapus seluruh wilayah!"
                Log.d(TAG, "deleteAllWilayah: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh wilayah!"
                Log.d(TAG, "deleteAllWilayah: $message (${e.message})")
            }
        }
    }
}