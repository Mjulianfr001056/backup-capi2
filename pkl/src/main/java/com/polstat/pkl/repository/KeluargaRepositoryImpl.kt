package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.mapper.toKeluargaEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KeluargaRepositoryImpl @Inject constructor(
    private val capi63Database: Capi63Database
) : KeluargaRepository {

    companion object {
        private const val TAG = "CAPI63_KLGREPOIMPL"
    }

    override suspend fun insertKeluarga(
        keluarga: Keluarga
    ): Flow<String> {
        return  flow {
            try {
                Log.d(TAG, "Keluarga: $keluarga")
                val readyKlg = keluarga.copy(status = "insert")
                Log.d(TAG, "Keluarga insert: $readyKlg")
                Log.d(TAG, "insertKeluarga: ${readyKlg.toKeluargaEntity()}")
                capi63Database.capi63Dao.insertKeluarga(readyKlg.toKeluargaEntity())
                val message = "Berhasil menambahkan keluarga!"
                emit(message)
            } catch (e: SQLiteConstraintException) {
                val message = "Gagal menambahkan keluarga! Constraint pelanggaran: ${e.message}"
                Log.d(TAG, "insertKeluarga: $message")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan mahasiswa! Kesalahan umum: ${e.message}"
                Log.d(TAG, "insertKeluarga: $message")
                emit(message)
            }
        }
    }

    override suspend fun updateKeluarga(keluarga: Keluarga): Flow<String> {
        return flow {
            try {
                Log.d(TAG, "Keluarga entity: ${keluarga.toKeluargaEntity()}")
                val updatedKlg = keluarga.copy(status = "update")
                capi63Database.capi63Dao.updateKeluarga(updatedKlg.toKeluargaEntity())
                val message = "Berhasil mengubah keluarga!"
                Log.d(TAG, "updateKeluarga: $message $keluarga")
                emit(message)
            } catch (e: SQLiteConstraintException) {
                val message = "Gagal mengubah keluarga! Constraint pelanggaran: ${e.message}"
                Log.d(TAG, "updateKeluarga: $message")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal mengubah keluarga! Kesalahan umum: ${e.message}"
                Log.d(TAG, "updateKeluarga: $message")
                emit(message)
            }
        }
    }

    override suspend fun getKeluarga(kodeKlg: String): Flow<Result<KeluargaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val keluarga = capi63Database.capi63Dao.getKeluarga(kodeKlg)

                Log.d(TAG, "Berhasil getKeluarga: $keluarga")

                emit(Result.Success(keluarga))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getKeluarga: ${e.message}")
                emit(Result.Error(null, "Error fetching Keluarga: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun deleteAllKeluarga(): Flow<String> {
        return  flow {
            try {
                capi63Database.capi63Dao.deleteAllKeluarga()
                val message = "Berhasil menghapus seluruh keluarga!"
                Log.d(TAG, "deleteAllKeluarga: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh keluarga!"
                Log.d(TAG, "deleteAllKeluarga: $message (${e.message})")
            }
        }
    }

    override suspend fun getKeluargaWithRuta(
        kodeKlg: String
    ): Flow<Result<KeluargaWithRuta>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val keluargaWithRuta = capi63Database.capi63Dao.getKeluargaWithRuta(kodeKlg)

                Log.d(TAG, "Berhasil getKeluargaWithRuta: $keluargaWithRuta")

                emit(Result.Success(keluargaWithRuta))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getKeluargaWithRuta: ${e.message}")
                emit(Result.Error(null, "Error fetching KeluargaWithRuta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

}