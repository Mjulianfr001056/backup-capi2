//package com.polstat.pkl.viewmodel
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Location
//import android.util.Log
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.android.gms.location.FusedLocationProviderClient
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class LocationViewModel @Inject constructor(
//    private val fusedLocationClient: FusedLocationProviderClient,
//    private val context: Context
//) : ViewModel() {
//    private val _countdown = MutableStateFlow(10 * 60 * 1000)
//    val countdown: StateFlow<Int> = _countdown.asStateFlow()
//    private val TAG = "Location"
//
//    init {
//        viewModelScope.launch {
//            while (true) {
//                delay(1000)
//                if (_countdown.value > 0) {
//                    _countdown.value--
//                } else {
//                    getLastLocation(fusedLocationClient, context, object : LocationCallback {
//                        override fun onLocationReceived(latitude: Double, longitude: Double) {
//                            Log.e(TAG, "Location ${latitude} ${longitude}")
//                        }
//                        override fun onLocationError(exception: Exception) {
//                            Log.e(TAG, "Error getting location", exception)
//                            Toast.makeText(context, "Gagal mendapatkan lokasi.", Toast.LENGTH_SHORT).show()
//                        }
//                        })
//                    _countdown.value = 10 * 60 * 1000
//                }
//            }
//        }
//    }
//
//    fun getLastLocation(
//        fusedLocationClient: FusedLocationProviderClient,
//        context: Context,
//        callback: LocationCallback
//    ) {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    val latitude = location.latitude
//                    val longitude = location.longitude
//                    callback.onLocationReceived(latitude, longitude)
//                }
//            }
//            .addOnFailureListener { e ->
//                callback.onLocationError(e)
//            }
//    }
//}