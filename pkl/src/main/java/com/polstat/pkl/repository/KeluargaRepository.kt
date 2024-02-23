package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.relation.KeluargaWithRuta
import com.polstat.pkl.model.domain.Keluarga
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface KeluargaRepository {

    suspend fun insertKeluarga(keluarga: Keluarga, method: Method) : Flow<String>

//    suspend fun fetchKeluargaFromServer(keluarga: Keluarga) : Flow<String>

    suspend fun getKeluarga(idBS: String, noSegmen: String, kodeKlg: String) : Flow<Result<KeluargaEntity>>

    suspend fun fakeDeleteKeluarga(keluarga: Keluarga) : Flow<String>

    suspend fun getAllKeluargaByWilayah(idBS: String) : Flow<Result<List<KeluargaEntity>>>

    suspend fun getAllKeluargaByRuta(kodeRuta: String) : Flow<Result<List<KeluargaEntity>>>

    suspend fun getKeluargaWithLastNoSegmen(idBS: String) : Flow<Result<KeluargaEntity>>
    suspend fun getLastKeluarga(idBS: String, noSegmen: String) : Flow<Result<KeluargaEntity>>

    suspend fun getLastKeluargaEgb(idBS: String, noSegmen: String) : Flow<Result<KeluargaEntity>>

    suspend fun updateKeluarga(keluarga: Keluarga) : Flow<String>

    suspend fun deleteAllKeluarga() : Flow<String>

    suspend fun deleteAllKeluargaByWilayah(idBS: String) : Flow<String>

    suspend fun getListKeluargaWithRuta(idBS: String) : Flow<Result<List<KeluargaWithRuta>>>

    suspend fun updateStatusKeluarga(kodeKlg: String) : Flow<String>

    sealed class Method {
        object Insert : Method()
        object Fetch : Method()

        fun retrieveMethod() : String {
            return when(this) {
                is Insert -> "insert"
                is Fetch -> "fetch"
                else -> {""}
            }
        }
    }

}