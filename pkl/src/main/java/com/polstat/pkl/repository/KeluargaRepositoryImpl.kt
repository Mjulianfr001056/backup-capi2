package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.mapper.toKeluargaEntity
import com.polstat.pkl.mapper.toRutaEntity
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
                val message = "insertKeluarga: Berhasil menambahkan keluarga! $readyKlg"

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

    override suspend fun fakeDeleteKeluarga(keluarga: Keluarga): Flow<String> {
        return  flow {
            try {
                val updatedKlg = keluarga.copy(status = "delete")
                capi63Dao.updateKeluarga(updatedKlg.toKeluargaEntity())
                val message = "Berhasil menghapus keluarga!"
                Log.d(TAG, "fakeDeleteKeluarga: $message $updatedKlg")
            } catch (e: Exception) {
                val message = "Gagal menghapus keluarga!"
                Log.d(TAG, "fakeDeleteKeluarga: $message (${e.message})")
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

    override suspend fun getKeluarga(idBS: String, noSegmen: String, kodeKlg: String): Flow<Result<KeluargaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val keluarga = capi63Dao.getKeluarga(idBS, noSegmen, kodeKlg)

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

    override suspend fun getLastKeluarga(idBS: String, noSegmen: String): Flow<Result<KeluargaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val lastKeluarga = capi63Dao.getLastKeluarga(idBS, noSegmen)

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

    override suspend fun getLastKeluargaEgb(idBS: String, noSegmen: String): Flow<Result<KeluargaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val lastKeluargaEgb = capi63Dao.getLastKeluargaEgb(idBS, noSegmen)

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

    override suspend fun getKeluargaWithLastNoSegmen(idBS: String): Flow<Result<KeluargaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val keluargaWithLastNoSegmen = capi63Dao.getKeluargaWithLastNoSegmen(idBS)

                Log.d(TAG, "Berhasil getKeluargaWithLastNoSegmen: $keluargaWithLastNoSegmen")

                emit(Result.Success(keluargaWithLastNoSegmen))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getKeluargaWithLastNoSegmen: ${e.message}")
                emit(Result.Error(null, "Error fetching KeluargaWithLastNoSegmen: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getAllKeluargaByWilayah(idBS: String): Flow<Result<List<KeluargaEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listAllKeluargaByWilayah = capi63Dao.getAllKeluargaByWilayah(idBS)
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
                val listAllKeluargaByRuta = capi63Dao.getAllKeluargaByRuta(kodeRuta)
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

    override suspend fun deleteAllKeluargaByWilayah(idBS: String): Flow<String> {
        return  flow {
            try {
                capi63Dao.deleteAllKeluargaByWilayah(idBS)
                val message = "Berhasil menghapus seluruh keluarga by wilayah!"
                Log.d(TAG, "deleteAllKeluargaByWilayah: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh keluarga by wilayah!"
                Log.d(TAG, "deleteAllKeluargaByWilayah: $message (${e.message})")
            }
        }
    }

    override suspend fun getListKeluargaWithRuta(idBS: String): Flow<Result<List<KeluargaWithRuta>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listKeluargaWithRuta = capi63Dao.getListKeluargaWithRuta(idBS)
                Log.d(TAG, "Berhasil getListKeluargaWithRuta: $listKeluargaWithRuta")
                emit(Result.Success(listKeluargaWithRuta))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getListKeluargaWithRuta: ${e.message}")
                emit(Result.Error(null, "Error Get List Keluarga With Ruta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun updateStatusKeluarga(kodeKlg: String): Flow<String> {
        return  flow {
            try {
                capi63Dao.updateStatusKeluarga(kodeKlg)
                val message = "Berhasil mengupdate keluarga!"
                Log.d(TAG, "updateStatusKeluarga: $message")
            } catch (e: Exception) {
                val message = "Gagal mengupdate keluarga!"
                Log.d(TAG, "updateStatusKeluarga: $message (${e.message})")
            }
        }
    }

}