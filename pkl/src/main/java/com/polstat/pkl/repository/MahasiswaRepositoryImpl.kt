package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.mapper.toMahasiswaEntity
import com.polstat.pkl.model.domain.Mahasiswa
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MahasiswaRepositoryImpl @Inject constructor (
    private val capi63Database: Capi63Database
) : MahasiswaRepository {

    companion object {
        private const val TAG = "CAPI63_MHSREPOIMPL"
    }

    override suspend fun insertMahasiswa(
        mahasiswa: Mahasiswa
    ): Flow<String> {
        return flow {
            try {
                Log.d(TAG, "Mahasiswa entity: ${mahasiswa.toMahasiswaEntity()}")
                capi63Database.capi63Dao.insertMahasiswa(mahasiswa.toMahasiswaEntity())
                val message = "Berhasil menambahkan mahasiswa!"
                Log.d(TAG, "insertMahasiswa: $message $mahasiswa")
                emit(message)
            } catch (e: SQLiteConstraintException) {
                val message = "Gagal menambahkan mahasiswa! Constraint pelanggaran: ${e.message}"
                Log.d(TAG, "insertMahasiswa: $message")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan mahasiswa! Kesalahan umum: ${e.message}"
                Log.d(TAG, "insertMahasiswa: $message")
                emit(message)
            }
        }
    }

    override suspend fun deleteAllMahasiswa(): Flow<String> {
        return  flow {
            try {
                capi63Database.capi63Dao.deleteAllMahasiswa()
                val message = "Berhasil menghapus seluruh mahasiswa!"
                Log.d(TAG, "deleteAllMahasiswa: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh mahasiswa!"
                Log.d(TAG, "deleteAllMahasiswa: $message (${e.message})")
            }
        }
    }
}