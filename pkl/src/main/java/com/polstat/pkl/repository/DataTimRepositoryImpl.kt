package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.relation.DataTimWithAll
import com.polstat.pkl.database.relation.DataTimWithMahasiswa
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.database.relation.MahasiswaWithAll
import com.polstat.pkl.database.relation.RutaWithKeluarga
import com.polstat.pkl.database.relation.WilayahWithAll
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

    override suspend fun getDataTimWithMahasiswa(
        idTim: String
    ): Flow<Result<DataTimWithMahasiswa>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val dataTimWithMahasiswa = dao.getDataTimWithMahasiswa(idTim)

                Log.d(TAG, "Berhasil getDataTimWithMahasiswa: $dataTimWithMahasiswa")

                emit(Result.Success(dataTimWithMahasiswa))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getDataTimWithMahasiswa: ${e.message}")
                emit(Result.Error(null, "Error fetching DataTimWithMahasiswa: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getDataTimWithAll(
        idTim: String
    ): Flow<Result<DataTimWithAll>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val dataTimWithMahasiswa = dao.getDataTimWithMahasiswa(idTim)

                val listMahasiswaWithAll = dataTimWithMahasiswa.listMahasiswa?.map { mahasiswa ->

                    val mahasiswaWithWilayah = dao.getMahasiswaWithWilayah(mahasiswa.nim)

                    val listWilayahWithAll = mahasiswaWithWilayah.listWilayah?.map { wilayah ->

                        val wilayahWithKeluarga = dao.getWilayahWithKeluarga(wilayah.noBS)

                        val wilayahWithRuta = dao.getWilayahWithRuta(wilayah.noBS)

                        val listKeluargaWithRuta = wilayahWithKeluarga.listKeluarga?.map { keluarga ->

                            val ruta = dao.getKeluargaWithRuta(keluarga.kodeKlg)

                            KeluargaWithRuta(keluarga, ruta.listRuta)

                        }

                        val listRutaWithKeluarga = wilayahWithRuta.listRuta?.map {ruta ->

                            val keluarga = dao.getRutaWithKeluarga(ruta.kodeRuta)

                            RutaWithKeluarga(ruta, keluarga.listKeluarga)

                        }

                        WilayahWithAll(wilayahWithKeluarga, wilayahWithRuta, listKeluargaWithRuta, listRutaWithKeluarga)

                    }

                    MahasiswaWithAll(mahasiswaWithWilayah, listWilayahWithAll)

                }

                val dataTimWithAll = DataTimWithAll(dataTimWithMahasiswa, listMahasiswaWithAll)

                Log.d(TAG, "Berhasil getDataTimWithAll: $dataTimWithAll")

                emit(Result.Success(dataTimWithAll))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getDataTimWithAll: ${e.message}")
                emit(Result.Error(null, "Error fetching DataTimWithAll: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

}