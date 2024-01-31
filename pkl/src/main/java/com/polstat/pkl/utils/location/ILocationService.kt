package com.polstat.pkl.utils.location

import com.polstat.pkl.model.domain.Lokasi
import kotlinx.coroutines.flow.Flow

interface ILocationService {

    fun requestLocationUpdates(): Flow<Lokasi?>

//    fun requestCurrentLocation(): Flow<Lokasi?>
}