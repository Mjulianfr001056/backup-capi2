package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.dao.Capi63Dao
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.mapper.toSampelRutaEntity
import com.polstat.pkl.model.domain.SampelRuta
import com.polstat.pkl.model.response.SampelRutaResponse
import com.polstat.pkl.network.SampelRutaApi
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class SampelRutaRepositoryImpl @Inject constructor(
    private val sampelRutaApi: SampelRutaApi,
    private val capi63Dao: Capi63Dao
) : SampelRutaRepository {

    companion object {
        private const val TAG = "CAPI63_SAMPELREPOIMPL"
    }

    override suspend fun getSampelRutaFromWS(
        idBS: String
    ): Flow<Result<SampelRutaResponse>> {
        return flow {
            emit(Result.Loading(true))
            val sampelRutaResponse = try {
                sampelRutaApi.getSampel(idBS)
            }  catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = e.localizedMessage ?: "Fetch Sampel Ruta Error"))
                Log.e(TAG, "getSampelRutaFromWS: Terjadi kesalahan!", e)
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    204 -> emit(Result.Error(message = "Data Sampel Tidak Ditemukan!"))
                    400 -> emit(Result.Error(message = "Data duplicate atau BS belum di finalisasi!"))
                    else -> emit(Result.Error(message = e.localizedMessage ?: "Fetch Sampel Ruta Error"))
                }
                Log.e(TAG, "getSampelRutaFromWS: Terjadi kesalahan!", e)
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = e.localizedMessage ?: "Fetch Sampel Ruta Error"))
                Log.e(TAG, "getSampelRutaFromWS: Terjadi kesalahan!", e)
                return@flow
            }

            Log.d(TAG, "Berhasil getSampelRutaFromWS! : $sampelRutaResponse")

            emit(Result.Success(sampelRutaResponse))
            emit(Result.Loading(false))
        }
    }

    override suspend fun insertSampelRuta(
        sampelRuta: SampelRuta
    ): Flow<String> {
        return flow {
            try {
                Log.d(TAG, "Sampel Ruta : ${sampelRuta.toSampelRutaEntity()}")
                capi63Dao.insertSampelRuta(sampelRuta.toSampelRutaEntity())
                val message = "Berhasil menambahkan sampel ruta!"
                Log.d(TAG, "insertSampelRuta: $message $sampelRuta")
                emit(message)
            } catch (e: SQLiteConstraintException) {
                val message = "Gagal menambahkan sampel ruta! Constraint pelanggaran: ${e.message}"
                Log.d(TAG, "insertSampelRuta: $message")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan sampel ruta! Kesalahan umum: ${e.message}"
                Log.d(TAG, "insertSampelRuta: $message")
                emit(message)
            }
        }
    }

    override suspend fun getSampelRuta(
        idBS: String
    ): Flow<Result<List<SampelRutaEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val sampelRuta = capi63Dao.getSampelRutaByNoBS(noBS)

                Log.d(TAG, "Berhasil getSampelRuta: $sampelRuta")

                emit(Result.Success(sampelRuta))
            }  catch (e: Exception) {
                Log.d(TAG, "Gagal getSampelRuta: ${e.message}")
                emit(Result.Error(null, "Error fetching Sampel Ruta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getAllSampelRuta(): Flow<Result<List<SampelRutaEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))
                val listAllSampelRuta = capi63Dao.getAllSampelRuta()
                Log.d(TAG, "Berhasil getAllSampelRuta: $listAllSampelRuta")
                emit(Result.Success(listAllSampelRuta))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getAllSampelRuta: ${e.message}")
                emit(Result.Error(null, "Error Get All Sampel Ruta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun confirmSampel(kodeRuta: String): Flow<String> {
        return flow {
            try {
                sampelRutaApi.confirmSampel(kodeRuta)
                val message = "Berhasil Konfirmasi Sampel"
                Log.d(TAG, "Konfirmasi Sampel berhasil !")
                emit(message)
            } catch (e: IOException) {
                val message = "Konfirmasi Sampel gagal !"
                Log.d(TAG, "Konfirmasi Sampel gagal !", e)
                emit(message)
                e.printStackTrace()
            } catch (e: HttpException) {
                val message = "Konfirmasi Sampel gagal !"
                Log.d(TAG, "Konfirmasi Sampel gagal !", e)
                emit(message)
                e.printStackTrace()
            } catch (e: Exception) {
                val message = "Konfirmasi Sampel gagal !"
                Log.d(TAG, "Konfirmasi Sampel gagal !", e)
                emit(message)
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteAllSampelRuta(): Flow<String> {
        return  flow {
            try {
                capi63Dao.deleteAllSampelRuta()
                val message = "Berhasil menghapus seluruh sampel ruta!"
                Log.d(TAG, "deleteAllSampelRuta: $message")
            } catch (e: Exception) {
                val message = "Gagal menghapus seluruh sampel ruta!"
                Log.d(TAG, "deleteAllSampelRuta: $message (${e.message})")
            }
        }
    }

}