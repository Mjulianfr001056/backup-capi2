package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.relation.DataTimWithAll
import com.polstat.pkl.database.relation.DataTimWithMahasiswa
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.database.relation.MahasiswaWithAll
import com.polstat.pkl.database.relation.WilayahWithAll
import com.polstat.pkl.mapper.toDataTimEntity
import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DataTimRepositoryImpl @Inject constructor (
    private val capi63Database: Capi63Database
) : DataTimRepository {

    companion object {
        private const val TAG = "CAPI63_DATATIMREPOIMPL"
    }

    override suspend fun insertDataTim(
        dataTim: DataTim
    ): Flow<String> {
        return  flow {
            try {
                capi63Database.capi63Dao.insertDataTim(dataTim.toDataTimEntity())
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

    override suspend fun getDataTim(
        idTim: String
    ): Flow<Result<DataTimEntity>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val dataTimEntity = capi63Database.capi63Dao.getDataTim(idTim)

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

    override suspend fun getDataTimWithMahasiswa(
        idTim: String
    ): Flow<Result<DataTimWithMahasiswa>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val dataTimWithMahasiswa = capi63Database.capi63Dao.getDataTimWithMahasiswa(idTim)

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

                val dataTimWithMahasiswa = capi63Database.capi63Dao.getDataTimWithMahasiswa(idTim)

                val listMahasiswaWithAll = dataTimWithMahasiswa.listMahasiswa?.map { mahasiswa ->

                    val mahasiswaWithWilayah = capi63Database.capi63Dao.getMahasiswaWithWilayah(mahasiswa.nim)

                    val listWilayahWithAll = mahasiswaWithWilayah.listWilayah?.map { wilayah ->

                        val wilayahWithKeluarga = capi63Database.capi63Dao.getWilayahWithKeluarga(wilayah.noBS)

                        val listKeluargaWithRuta = wilayahWithKeluarga.listKeluarga?.map { keluarga ->

                            val ruta = capi63Database.capi63Dao.getKeluargaWithRuta(keluarga.kodeKlg)

                            KeluargaWithRuta(keluarga, ruta.listRuta)

                        }

                        WilayahWithAll(wilayahWithKeluarga, listKeluargaWithRuta)

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