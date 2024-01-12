package com.polstat.pkl.model.request

import com.polstat.pkl.model.domain.RutaDto

data class SyncRutaRequest (
    val nim: String = "",
    val noBS: String = "",
    val json: List<RutaDto> = emptyList()
)