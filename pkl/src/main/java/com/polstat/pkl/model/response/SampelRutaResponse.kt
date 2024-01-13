package com.polstat.pkl.model.response

import com.polstat.pkl.model.domain.SampelRuta

data class SampelRutaResponse(
    val SampelRuta: List<SampelRuta> = emptyList()
)