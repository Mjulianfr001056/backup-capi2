package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalRutaRepository {

    fun insertRuta(ruta: Ruta) : Flow<String>

    fun insertKeluargaAndRuta(kodeKlg: String, kodeRuta: String) : Flow<String>

    fun updateRuta(ruta: Ruta) : Flow<String>

    fun fakeDeleteRuta(ruta: Ruta) : Flow<String>

    fun getRuta(kodeRuta: String) : Flow<Result<RutaEntity>>
}