package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.mapper.toKeluargaEntity
import com.polstat.pkl.mapper.toMahasiswaEntity
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
                Log.d(TAG, "Keluarga entity: ${keluarga.toKeluargaEntity()}")
                capi63Database.capi63Dao.insertKeluarga(keluarga.toKeluargaEntity())
                val message = "Berhasil menambahkan keluarga!"
                Log.d(TAG, "insertKeluarga: $message $keluarga")
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