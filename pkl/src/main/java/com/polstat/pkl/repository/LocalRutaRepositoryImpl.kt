package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.mapper.toRutaEntity
import com.polstat.pkl.model.domain.Ruta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRutaRepositoryImpl @Inject constructor(
    private val capi63Database: Capi63Database
) : LocalRutaRepository {

    companion object {
        private const val TAG = "LocalRutaRepoImpl"
    }

    override fun insertRuta(
        ruta: Ruta
    ): Flow<String> {
        return  flow {
            try {
                val readyRuta = ruta.copy(status = "insert")
                capi63Database.capi63Dao.insertRuta(readyRuta.toRutaEntity())
                val message = "Berhasil menambahkan ruta!"
                Log.d(TAG, "insertRuta: $message")
            } catch (e: Exception) {
                val message = "Gagal menambahkan ruta!"
                Log.d(TAG, "insertRuta: $message (${e.message})")
            }
        }
    }

    override fun updateRuta(ruta: Ruta): Flow<String> {
        return  flow {
            try {
                val readyRuta = ruta.copy(status = "update")
                capi63Database.capi63Dao.updateRuta(readyRuta.toRutaEntity())
                val message = "Berhasil mengupdate ruta!"
                Log.d(TAG, "updateRuta: $message")
            } catch (e: Exception) {
                val message = "Gagal mengupdate ruta!"
                Log.d(TAG, "updateRuta: $message (${e.message})")
            }
        }
    }

    override fun fakeDeleteRuta(ruta: Ruta): Flow<String> {
        return  flow {
            try {
                val readyRuta = ruta.copy(status = "delete")
                capi63Database.capi63Dao.updateRuta(readyRuta.toRutaEntity())
                val message = "Berhasil menghapus ruta!"
                Log.d(TAG, "fakeDeleteRuta: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus ruta!"
                Log.d(TAG, "fakeDeleteRuta: $message (${e.message})")
            }
        }
    }

}