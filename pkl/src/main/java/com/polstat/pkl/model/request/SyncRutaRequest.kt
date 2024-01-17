package com.polstat.pkl.model.request

data class SyncRutaRequest (
    val nim: String? = "",
    val no_bs: String? = "",
    val json: JsonKlg? = null
)