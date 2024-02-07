package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.mapper.toKeluargaEntity
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KeluargaRepositoryImpl @Inject constructor(
    private val capi63Dao: Capi63Dao
) : KeluargaRepository {

    companion object {
        private const val TAG = "CAPI63_KLGREPOIMPL"
    }

    override suspend fun insertKeluarga(
        keluarga: Keluarga,
        method: KeluargaRepository.Method
    ): Flow<String> {
        return  flow {
            try {
                val status = method.retrieveMethod()
                val readyKlg = keluarga.copy(status = status)
                val message = "Berhasil menambahkan keluarga!"

                capi63Dao.insertKeluarga(readyKlg.toKeluargaEntity())
                emit(message)
            } catch (e: SQLiteConstraintException) {
                val message = "Gagal menambahkan keluarga! Constraint pelanggaran: ${e.message}"
                Log.d(TAG, "insertKeluarga: $message")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan keluarga! Kesalahan umum: ${e.message}"
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
                capi63Dao.updateKeluarga(updatedKlg.toKeluargaEntity())
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

                val keluarga = capi63Dao.getKeluarga(kodeKlg)

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

    override suspend fun getLastKeluarga(): Flow<Result<KeluargaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val lastKeluarga = capi63Dao.getLastKeluarga()

                Log.d(TAG, "Berhasil getLastKeluarga: $lastKeluarga")

                emit(Result.Success(lastKeluarga))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getLastKeluarga: ${e.message}")
                emit(Result.Error(null, "Error fetching Last Keluarga: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getLastKeluargaEgb(): Flow<Result<KeluargaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val lastKeluargaEgb = capi63Dao.getLastKeluargaEgb()

                Log.d(TAG, "Berhasil getLastKeluargaEgb: $lastKeluargaEgb")

                emit(Result.Success(lastKeluargaEgb))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getLastKeluargaEgb: ${e.message}")
                emit(Result.Error(null, "Error fetching Last Keluarga Egb: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getAllKeluargaByWilayah(idBS: String): Flow<Result<List<KeluargaEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listAllKeluargaByWilayah = capi63Database.capi63Dao.getAllKeluargaByWilayah(idBS)
                Log.d(TAG, "Berhasil getAllKeluargaByWilayah: $listAllKeluargaByWilayah")
                emit(Result.Success(listAllKeluargaByWilayah))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getAllKeluargaByWilayah: ${e.message}")
                emit(Result.Error(null, "Error Get All Keluarga By Wilayah: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getAllKeluargaByRuta(kodeRuta: String): Flow<Result<List<KeluargaEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listAllKeluargaByRuta = capi63Database.capi63Dao.getAllKeluargaByRuta(kodeRuta)
                Log.d(TAG, "Berhasil getAllKeluargaByRuta: $listAllKeluargaByRuta")
                emit(Result.Success(listAllKeluargaByRuta))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getAllKeluargaByRuta: ${e.message}")
                emit(Result.Error(null, "Error Get All Keluarga By Ruta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun deleteAllKeluarga(): Flow<String> {
        return  flow {
            try {
                capi63Dao.deleteAllKeluarga()
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

                val keluargaWithRuta = capi63Dao.getKeluargaWithRuta(kodeKlg)

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