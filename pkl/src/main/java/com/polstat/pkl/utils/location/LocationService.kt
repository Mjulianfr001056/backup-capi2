package com.polstat.pkl.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.polstat.pkl.model.domain.Lokasi
import com.polstat.pkl.utils.extension.hasLocationPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationService @Inject constructor(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient
): ILocationService {

    @SuppressLint("MissingPermission")
    override fun requestLocationUpdates(): Flow<Lokasi?> = callbackFlow {

        if (!context.hasLocationPermission()) {
            trySend(null)
//            return@callbackFlow
        }

        val request = LocationRequest.Builder(600000L)
            .setIntervalMillis(600000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let {
                    trySend(Lokasi(it.latitude, it.longitude, it.accuracy))
                    Log.i("locationExample", "onLocationResult: $it")
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            stopLocationUpdates(locationCallback)
        }
    }

    private fun stopLocationUpdates(locationCallback: LocationCallback) {
        locationClient.removeLocationUpdates(locationCallback)
    }

}