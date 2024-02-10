package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.database.relation.RutaWithKeluarga
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalRutaRepository {

    suspend fun insertRuta(ruta: Ruta, method: Method) : Flow<String>

//    suspend fun fetchRutaFromServer(ruta: Ruta) : Flow<String>

    suspend fun insertKeluargaAndRuta(kodeKlg: String, kodeRuta: String) : Flow<String>

//    suspend fun deleteAllKeluargaAndRutaByWilayah(idBS: String) : Flow<String>

    suspend fun deleteAllKeluargaRutaAndRelationByWilayah(idBS: String) : Flow<String>

    suspend fun deleteAllKeluargaAndRuta() : Flow<String>

    suspend fun updateRuta(ruta: Ruta) : Flow<String>

    suspend fun deleteAllRuta() : Flow<String>

    suspend fun deleteAllRutaByWilayah(idBS: String) : Flow<String>

    suspend fun fakeDeleteRuta(ruta: Ruta) : Flow<String>

    suspend fun getRuta(kodeRuta: String) : Flow<Result<RutaEntity>>

    suspend fun getAllRutaByWilayah(idBS: String) : Flow<Result<List<RutaEntity>>>

    suspend fun getLastRuta() : Flow<Result<RutaEntity>>

    suspend fun getAllRutaByKeluarga(kodeKlg: String) : Flow<Result<List<RutaEntity>>>

    suspend fun getListRutaWithKeluarga(idBS: String) : Flow<Result<List<RutaWithKeluarga>>>

    sealed class Method {
        object Insert : Method()
        object Fetch : Method()

        fun retrieveMethod() : String {
            return when(this) {
                is Insert -> "insert"
                is Fetch -> "fetch"
            }
        }
    }
}