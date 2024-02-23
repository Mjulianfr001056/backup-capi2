package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.model.request.SyncRutaRequest
import com.polstat.pkl.model.response.FinalisasiBSResponse
import com.polstat.pkl.model.response.SyncRutaResponse
import com.polstat.pkl.network.RutaApi
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class RemoteRutaRepositoryImpl @Inject constructor(
    private val rutaApi: RutaApi
) : RemoteRutaRepository {

    companion object {
        private const val TAG = "CAPI63_REMRUTAREPOIMPL"
    }

    override suspend fun sinkronisasiRuta(syncRutaRequest: SyncRutaRequest): Flow<Result<SyncRutaResponse>> {
        return flow {
            emit(Result.Loading(true))
            val syncRutaResponse = try {
                Log.d(TAG, "Sync Ruta Request:  $syncRutaRequest")
                val response = rutaApi.sinkronisasiRuta(syncRutaRequest)
                Log.d(TAG, "Sync Ruta Response:  $response")
                response
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = e.localizedMessage ?: "Synchronization Error"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(message = e.localizedMessage ?: "Synchronization Error"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = e.localizedMessage ?: "Synchronization Error"))
                return@flow
            }

            emit(Result.Success(syncRutaResponse))
            emit(Result.Loading(false))
        }
    }

    override suspend fun generateRuta(noBS: String): Flow<String> {
        return flow {
            try{
                rutaApi.generateSampel(noBS)
                val message = "Berhasil Ambil Sampel!"
                Log.d(TAG, message)
                emit(message)
            } catch (e: IOException) {
                val message = "Ambil Sampel gagal!: ${e.message}"
                Log.d(TAG, message, e)
                emit(message)
                e.printStackTrace()
            } catch (e: HttpException) {
//                val message = if (statusBS == "listing" && e.code() == 400) {
//                    "Gagal ambil sampel: Blok Sensus belum di finalisasi!"
//                } else if (statusBS == "telah-disampel" && e.code() == 400) {
//                    "Gagal ambil sampel: Blok Sensus sudah pernah diambil sampel!"
//                } else {
//                    "Gagal ambil sampel!: ${e.message}"
//                }

                val message = "Gagal ambil sampel!: ${e.message}"

                Log.d(TAG, message, e)
                emit(message)
                e.printStackTrace()
            } catch (e: Exception) {
                val message = "Ambil Sampel gagal!: ${e.message}"
                Log.d(TAG, message, e)
                emit(message)
                e.printStackTrace()
            }
        }
    }

    override suspend fun finalisasiBS(
        noBS: String
    ): Flow<Result<FinalisasiBSResponse>> {
        return flow {
            emit(Result.Loading(true))
            val finalBSResponse = try {
                val response = rutaApi.finalisasiBS(noBS)
                Log.d(TAG, "Berhasil melakukan finalisasi blok sensus!")
                response
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = ("Gagal finalisasi blok sensus!" + e.localizedMessage)))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(message = ("Gagal finalisasi blok sensus!" + e.localizedMessage)))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = ("Gagal finalisasi blok sensus!" + e.localizedMessage)))
                return@flow
            }

            emit(Result.Success(finalBSResponse))
            emit(Result.Loading(false))
        }
    }

}