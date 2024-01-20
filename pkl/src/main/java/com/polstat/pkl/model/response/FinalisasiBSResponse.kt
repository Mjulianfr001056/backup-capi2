package com.polstat.pkl.model.response

import com.polstat.pkl.model.domain.Ruta

data class FinalisasiBSResponse(
    val count: Int? = 0,
    val data: List<Ruta>? = emptyList(),
    val status: String? = ""
)