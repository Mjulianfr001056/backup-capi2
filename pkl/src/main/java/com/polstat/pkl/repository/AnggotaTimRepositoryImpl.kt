package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.mapper.toEntity
import com.polstat.pkl.model.domain.AnggotaTim
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnggotaTimRepositoryImpl @Inject constructor(
    private val dao: Capi63Dao
): AnggotaTimRepository{

    private val TAG = "CAPI63_ANGGOTA_TIM_REPO"
    override suspend fun insertAnggotaTim(anggota: AnggotaTim): Flow<String> {
        return flow {
            try {
                val anggotaTim = anggota.toEntity()
                dao.insertAnggotaTim(anggotaTim)

                val message = "${anggota.nama} berhasil ditambahkan ke dalam tim"
                emit(message)
            }catch (e: Exception){
                val message = "Gagal menambahkan ${anggota.nama} ke dalam tim"
                Log.d(this@AnggotaTimRepositoryImpl.TAG, "error: $message (${e.message})")
                emit(message)
                return@flow
            }
        }
    }

    override suspend fun getAllAnggotaTim(): Flow<Result<List<AnggotaTimEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listAnggotaTim = dao.getAllAnggotaTim()
                Log.d(TAG, "Berhasil getAllAnggotaTim: $listAnggotaTim")
                emit(Result.Success(listAnggotaTim))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getAllAnggotaTim: ${e.message}")
                emit(Result.Error(null, "Error Get All Anggota Tim: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun deleteAllAnggotaTim(): Flow<String> {
        return  flow {
            try {
                dao.deleteAllAnggotaTim()
                val message = "Berhasil menghapus seluruh anggota tim!"
                Log.d(TAG, "deleteAllAnggotaTim: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh anggota tim!"
                Log.d(TAG, "deleteAllAnggotaTim: $message (${e.message})")
            }
        }
    }
}