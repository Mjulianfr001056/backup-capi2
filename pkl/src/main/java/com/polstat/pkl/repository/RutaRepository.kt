package com.polstat.pkl.repository

import com.polstat.pkl.model.domain.Ruta

interface RutaRepository {

    suspend fun getAllRuta(noBS: String): List<Ruta>
    suspend fun getRuta(kodeRuta: String): Ruta
    suspend fun addRuta(status: String, ruta: Ruta)
    suspend fun editRuta(status: String, kodeRuta: String): Ruta
    suspend fun deleteRuta(status: String, kodeRuta: String)

}