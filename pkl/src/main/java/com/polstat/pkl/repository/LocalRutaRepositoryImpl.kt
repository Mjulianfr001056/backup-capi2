package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.KeluargaAndRutaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.relation.RutaWithKeluarga
import com.polstat.pkl.mapper.toRutaEntity
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRutaRepositoryImpl @Inject constructor(
    private val capi63Dao: Capi63Dao
) : LocalRutaRepository {

    companion object {
        private const val TAG = "CAPI63_LCLRUTAREPOIMPL"
    }

    override suspend fun insertRuta(
        ruta: Ruta,
        method: LocalRutaRepository.Method
    ): Flow<String> {
        return  flow {
            try {
                val status = method.retrieveMethod()
                val readyRuta = ruta.copy(status = status)
                capi63Dao.insertRuta(readyRuta.toRutaEntity())
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

//    override suspend fun fetchRutaFromServer(ruta: Ruta): Flow<String> {
//        return  flow {
//            try {
//                val readyRuta = ruta.copy(status = "fetch")
//                capi63Dao.insertRuta(readyRuta.toRutaEntity())
//                val message = "Berhasil fetching ruta!"
//                Log.d(TAG, "fetchRuta: $message $readyRuta")
//                emit(message)
//            } catch (e: Exception) {
//                val message = "Gagal fetching ruta!"
//                Log.d(TAG, "fetchRuta: $message (${e.message})")
//                emit(message)
//                return@flow
//            }
//        }
//    }

    override suspend fun insertKeluargaAndRuta(
        kodeKlg: String,
        kodeRuta: String
    ): Flow<String> {
        return  flow {
            try {
                val keluargaAndRutaEntity = KeluargaAndRutaEntity(
                    kodeKlg = kodeKlg,
                    kodeRuta = kodeRuta
                )
                capi63Dao.insertKelurgaAndRuta(keluargaAndRutaEntity)
                val message = "Berhasil menambahkan KeluargaAndRuta!"
                Log.d(TAG, "insertKeluargaAndRuta: $message $keluargaAndRutaEntity")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan KeluargaAndRuta!"
                Log.d(TAG, "insertKeluargaAndRuta: $message (${e.message})")
                emit(message)
                return@flow
            }
        }
    }

    override suspend fun deleteAllKeluargaAndRuta(): Flow<String> {
        return  flow {
            try {
                capi63Dao.deleteAllKeluargaAndRuta()
                val message = "Berhasil menghapus seluruh keluargaandruta!"
                Log.d(TAG, "deleteAllKeluargaAndRuta: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh keluargaandruta!"
                Log.d(TAG, "deleteAllKeluargaAndRuta: $message (${e.message})")
            }
        }
    }

    override suspend fun updateRuta(ruta: Ruta): Flow<String> {
        return  flow {
            try {
                val readyRuta = ruta.copy(status = "update")
                capi63Dao.updateRuta(readyRuta.toRutaEntity())
                val message = "Berhasil mengupdate ruta!"
                Log.d(TAG, "updateRuta: $message $readyRuta")
            } catch (e: Exception) {
                val message = "Gagal mengupdate ruta!"
                Log.d(TAG, "updateRuta: $message (${e.message})")
            }
        }
    }

    override suspend fun fakeDeleteRuta(ruta: Ruta): Flow<String> {
        return  flow {
            try {
                val updatedRuta = ruta.copy(status = "delete")
                capi63Dao.updateRuta(updatedRuta.toRutaEntity())
                val message = "Berhasil menghapus ruta!"
                Log.d(TAG, "fakeDeleteRuta: $message $updatedRuta")
            } catch (e: Exception) {
                val message = "Gagal menghapus ruta!"
                Log.d(TAG, "fakeDeleteRuta: $message (${e.message})")
            }
        }
    }

    override suspend fun getRuta(
        kodeRuta: String
    ): Flow<Result<RutaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val rutaEntity = capi63Dao.getRuta(kodeRuta)

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

    override suspend fun getLastRuta(idBS: String): Flow<Result<RutaEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val lastRuta = capi63Dao.getLastRuta(idBS)

                Log.d(TAG, "Berhasil getLastRuta: $lastRuta")

                emit(Result.Success(lastRuta))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getLastRuta: ${e.message}")
                emit(Result.Error(null, "Error fetching Last Ruta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getAllRutaByWilayah(idBS: String): Flow<Result<List<RutaEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listAllRutaByWilayah = capi63Dao.getAllRutaByWilayah(idBS)
                Log.d(TAG, "Berhasil getAllRutaByWilayah: $listAllRutaByWilayah")
                emit(Result.Success(listAllRutaByWilayah))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getAllRutaByWilayah: ${e.message}")
                emit(Result.Error(null, "Error Get All Ruta By Wilayah: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getAllRutaByKeluarga(kodeKlg: String): Flow<Result<List<RutaEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listAllRutaByKeluarga = capi63Dao.getAllRutaByKeluarga(kodeKlg)
                Log.d(TAG, "Berhasil getAllRutaByKeluarga: $listAllRutaByKeluarga")
                emit(Result.Success(listAllRutaByKeluarga))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getAllRutaByKeluarga: ${e.message}")
                emit(Result.Error(null, "Error Get All Ruta By Keluarga: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun deleteAllRuta(): Flow<String> {
        return  flow {
            try {
                capi63Dao.deleteAllRuta()
                val message = "Berhasil menghapus seluruh ruta!"
                Log.d(TAG, "deleteAllRuta: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh ruta!"
                Log.d(TAG, "deleteAllRuta: $message (${e.message})")
            }
        }
    }

    override suspend fun deleteAllRutaByWilayah(idBS: String): Flow<String> {
        return  flow {
            try {
                capi63Dao.deleteAllRutaByWilayah(idBS)
                val message = "Berhasil menghapus seluruh ruta by wilayah!"
                Log.d(TAG, "deleteAllRutaByWilayah: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh ruta by wilayah!"
                Log.d(TAG, "deleteAllRutaByWilayah: $message (${e.message})")
            }
        }
    }

    override suspend fun deleteAllKeluargaRutaAndRelationByWilayah(idBS: String): Flow<String> {
        return  flow {
            try {
                capi63Dao.deleteAllKeluargaRutaAndRelationByWilayah(idBS)
                val message = "Berhasil menghapus seluruh keluarga ruta relasi by wilayah!"
                Log.d(TAG, "deleteAllKeluargaRutaAndRelationByWilayah: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh keluarga ruta relasi by wilayah!"
                Log.d(TAG, "deleteAllKeluargaRutaAndRelationByWilayah: $message (${e.message})")
            }
        }
    }
    override suspend fun getListRutaWithKeluarga(idBS: String): Flow<Result<List<RutaWithKeluarga>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listRutaWithKeluarga = capi63Dao.getListRutaWithKeluarga(idBS)
                Log.d(TAG, "Berhasil getListRutaWithKeluarga: $listRutaWithKeluarga")
                emit(Result.Success(listRutaWithKeluarga))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getListRutaWithKeluarga: ${e.message}")
                emit(Result.Error(null, "Error Get List Ruta With Keluarga: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

}