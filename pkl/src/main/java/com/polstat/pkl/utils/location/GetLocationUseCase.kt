package com.polstat.pkl.utils.location

import android.os.Build
import androidx.annotation.RequiresApi
import com.polstat.pkl.model.domain.Lokasi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val locationService: ILocationService
) {
    @RequiresApi(Build.VERSION_CODES.S)
    operator fun invoke(): Flow<Lokasi?> = locationService.requestLocationUpdates()

}