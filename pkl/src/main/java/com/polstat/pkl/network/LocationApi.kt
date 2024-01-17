package com.polstat.pkl.network

import com.polstat.pkl.model.request.UpdatePosisiRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationApi {
    @POST("updateposisi")
    suspend fun updateLocationPCL(
        @Body
        updatePosisiRequest: UpdatePosisiRequest
    )
}