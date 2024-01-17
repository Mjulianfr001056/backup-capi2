package com.polstat.pkl.model.request

data class UpdatePosisiRequest(
    val nim: String = "",
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
    val akurasi: Float? = 0.0F
)