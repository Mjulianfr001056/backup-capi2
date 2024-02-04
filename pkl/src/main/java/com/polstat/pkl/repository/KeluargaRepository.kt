package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface KeluargaRepository {

    suspend fun insertKeluarga(keluarga: Keluarga) : Flow<String>

    suspend fun fetchKeluargaFromServer(keluarga: Keluarga) : Flow<String>

    suspend fun getKeluarga(kodeKlg: String) : Flow<Result<KeluargaEntity>>

    suspend fun getLastKeluarga() : Flow<Result<KeluargaEntity>>

    suspend fun getLastKeluargaEgb() : Flow<Result<KeluargaEntity>>

    suspend fun updateKeluarga(keluarga: Keluarga) : Flow<String>

    suspend fun deleteAllKeluarga() : Flow<String>

    suspend fun getKeluargaWithRuta(kodeKlg: String) : Flow<Result<KeluargaWithRuta>>

}