package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.mapper.toDataTimEntity
import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.model.domain.Tim
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DataTimRepositoryImpl @Inject constructor (
    private val dao: Capi63Dao,
) : DataTimRepository {

    companion object {
        private const val TAG = "CAPI63_DATA-TIM-REPO"
    }

    @Deprecated("Use insertTim instead")
    override suspend fun insertDataTim(
        dataTim: DataTim
    ): Flow<String> {
        return  flow {
            try {
                dao.insertDataTim(dataTim.toDataTimEntity())
                val message = "Berhasil menambahkan data tim!"
                Log.d(TAG, "insertDataTim: $message $dataTim")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan data tim!"
                Log.d(TAG, "insertDataTim: $message (${e.message})")
                emit(message)
                return@flow
            }
        }
    }

    override suspend fun insertTim(
        tim: Tim
    ): Flow<String> {
        return flow {
            try {
                dao.insertDataTim(tim.toEntity())
                val message = "Berhasil menambahkan data tim!"
                Log.d(TAG, "insertDataTim: $message $tim")
                emit(message)
            }catch (e: Exception){
                val message = "Gagal menambahkan data tim!"
                Log.d(TAG, "insertTim: $message (${e.message})")
                emit(message)
                return@flow
            }
        }
    }

    //TODO(Pertimbangkan pakai model daripada pair)
    override suspend fun insertAnggotaTim(anggota: Pair<String, String>): Flow<String> {
        return flow {
            try {
                val anggotaTim = AnggotaTimEntity(anggota.first, anggota.second)
                dao.insertAnggotaTim(anggotaTim)

                val message = "Berhasil menambahkan data tim!"
                emit(message)
            }catch (e: Exception){
                val message = "Gagal menambahkan data tim!"
                Log.d(TAG, "insertAnggota: $message (${e.message})")
                emit(message)
                return@flow
            }
        }
    }

    override suspend fun getDataTim(
        idTim: String
    ): Flow<Result<DataTimEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val dataTimEntity = dao.getDataTim(idTim)

                Log.d(TAG, "Berhasil getDataTim: $dataTimEntity")

                emit(Result.Success(dataTimEntity))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getDataTim: ${e.message}")
                emit(Result.Error(null, "Error fetching DataTim: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun deleteAllDataTim(): Flow<String> {
        return  flow {
            try {
                dao.deleteAllDataTim()
                val message = "Berhasil menghapus seluruh data tim!"
                Log.d(TAG, "deleteAllDataTim: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh data tim!"
                Log.d(TAG, "deleteAllDataTim: $message (${e.message})")
            }
        }
    }
}