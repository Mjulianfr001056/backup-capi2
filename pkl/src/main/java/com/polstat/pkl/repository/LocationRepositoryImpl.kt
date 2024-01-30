package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.model.request.UpdatePosisiRequest
import com.polstat.pkl.network.LocationApi
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationApi: LocationApi,
) : LocationRepository {

    companion object {
        private const val TAG = "LocationRepositoryImpl"
    }

    override suspend fun updateLocation(
        nim: String,
        longitude: Double?,
        latitude: Double?,
        akurasi: Float?
    ){
        try{

            val updatePosisiRequest = UpdatePosisiRequest(
                nim,latitude,longitude,akurasi
            )
            locationApi.updateLocationPCL(updatePosisiRequest)
            Log.d(TAG, "Update location berhasil !")
        } catch (e: IOException) {
            Log.d(TAG, "Update location gagal !", e)
            e.printStackTrace()
        } catch (e: HttpException) {
            Log.d(TAG, "Update location gagal !", e)
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}