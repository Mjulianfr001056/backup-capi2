package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.model.request.SyncRutaRequest
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
                val message = "Berhasil Generate Sampel"
                Log.d(TAG, "Generate Sampel berhasil !")
                emit(message)
            } catch (e: IOException) {
                val message = "Generate Sampel gagal !"
                Log.d(TAG, "Generate Sampel gagal !", e)
                emit(message)
                e.printStackTrace()
            } catch (e: HttpException) {
                val message = "Generate Sampel gagal !"
                Log.d(TAG, "Generate Sampel gagal !", e)
                emit(message)
                e.printStackTrace()
            } catch (e: Exception) {
                val message = "Generate Sampel gagal !"
                Log.d(TAG, "Generate Sampel gagal !", e)
                emit(message)
                e.printStackTrace()
            }
        }
    }

}