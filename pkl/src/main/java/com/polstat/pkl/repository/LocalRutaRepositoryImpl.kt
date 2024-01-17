package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.mapper.toRutaEntity
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRutaRepositoryImpl @Inject constructor(
    private val capi63Database: Capi63Database
) : LocalRutaRepository {

    companion object {
        private const val TAG = "CAPI63_LCLRUTAREPOIMPL"
    }

    override fun insertRuta(
        ruta: Ruta
    ): Flow<String> {
        return  flow {
            try {
                val readyRuta = ruta.copy(status = "insert")
                capi63Database.capi63Dao.insertRuta(readyRuta.toRutaEntity())
                val message = "Berhasil menambahkan ruta!"
                Log.d(TAG, "insertRuta: $message $readyRuta")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan ruta!"
                Log.d(TAG, "insertRuta: $message (${e.message})")
                emit(message)
                return@flow
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

    override fun getRuta(
        kodeRuta: String
    ): Flow<Result<RutaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val rutaEntity = capi63Database.capi63Dao.getRuta(kodeRuta)

                Log.d(TAG, "Berhasil getKodeRuta: $rutaEntity")

                emit(Result.Success(rutaEntity))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getKodeRuta: ${e.message}")
                emit(Result.Error(null, "Error fetching DataRuta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }
}