package com.polstat.pkl.utils.location

import com.polstat.pkl.model.domain.Lokasi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val locationService: ILocationService
) {
    operator fun invoke(): Flow<Lokasi?> = locationService.requestLocationUpdates()

}