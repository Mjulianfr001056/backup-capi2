package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.Capi63Database
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
    private val capi63Database: Capi63Database
) : SampelRutaRepository {

    companion object {
        private const val TAG = "CAPI63_SAMPELREPOIMPL"
    }

    override suspend fun getSampelRutaFromWS(
        noBS: String
    ): Flow<Result<SampelRutaResponse>> {
        return flow {
            emit(Result.Loading(true))
            val sampelRutaResponse = try {
                sampelRutaApi.getSampel(noBS)
            }  catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = e.localizedMessage ?: "Fetch Sampel Ruta Error"))
                Log.e(TAG, "getSampelRutaFromWS: Terjadi kesalahan!", e)
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    204 -> emit(Result.Error(message = "Data Sampel Tidak Ditemukan"))
                    400 -> emit(Result.Error(message = "Terjadi Kesalahan Pengambilan Data"))
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
                capi63Database.capi63Dao.insertSampelRuta(sampelRuta.toSampelRutaEntity())
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
        noBS: String
    ): Flow<Result<List<SampelRutaEntity>>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val sampelRuta = capi63Database.capi63Dao.getSampelRutaByNoBS(noBS)

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

}