package com.polstat.pkl.repository

import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.network.RutaApi
import javax.inject.Inject

class RutaRepositoryImpl @Inject constructor(
    private val rutaApi: RutaApi
): RutaRepository{

    companion object {
        private const val TAG = "RutaRepositoryImpl"
    }

    override suspend fun getAllRuta(noBS: String): List<Ruta> {
        TODO("Not yet implemented")
    }

//    override suspend fun getAllRuta(): List<Ruta> = rutaApi.rutaSynchronize()

    override suspend fun getRuta(kodeRuta: String): Ruta {
        TODO("Not yet implemented")
    }

    override suspend fun addRuta(
        status: String,
        ruta: Ruta
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun editRuta(
        status: String,
        kodeRuta: String
    ): Ruta {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRuta(
        status: String,
        kodeRuta: String
    ) {
        TODO("Not yet implemented")
    }

}