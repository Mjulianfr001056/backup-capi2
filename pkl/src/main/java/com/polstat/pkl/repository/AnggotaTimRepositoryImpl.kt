package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.mapper.toEntity
import com.polstat.pkl.model.domain.AnggotaTim
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnggotaTimRepositoryImpl @Inject constructor(
    private val dao: Capi63Dao
): AnggotaTimRepository{

    private val TAG = "CAPI63_ANGGOTA_TIM_REPO"
    override suspend fun insert(anggota: AnggotaTim): Flow<String> {
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
}