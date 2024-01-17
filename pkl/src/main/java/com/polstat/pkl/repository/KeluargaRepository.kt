package com.polstat.pkl.repository

import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface KeluargaRepository {

    suspend fun insertKeluarga(keluarga: Keluarga) : Flow<String>

    suspend fun getKeluargaWithRuta(kodeKlg: String) : Flow<Result<KeluargaWithRuta>>

}