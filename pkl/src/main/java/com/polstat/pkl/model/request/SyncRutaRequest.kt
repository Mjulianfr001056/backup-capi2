package com.polstat.pkl.model.request

import com.polstat.pkl.model.domain.KeluargaDto

data class SyncRutaRequest (
    val nim: String? = "",
    val id_bs: String? = "",
    val json: List<KeluargaDto>? = emptyList()
)