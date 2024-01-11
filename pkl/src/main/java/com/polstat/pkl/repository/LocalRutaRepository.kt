package com.polstat.pkl.repository

import com.polstat.pkl.model.domain.Ruta
import kotlinx.coroutines.flow.Flow

interface LocalRutaRepository {

    fun insertRuta(ruta: Ruta) : Flow<String>

    fun updateRuta(ruta: Ruta) : Flow<String>

    fun fakeDeleteRuta(ruta: Ruta) : Flow<String>


}