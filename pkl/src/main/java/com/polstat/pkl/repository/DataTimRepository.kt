package com.polstat.pkl.repository

import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.database.relation.DataTimWithAll
import com.polstat.pkl.database.relation.DataTimWithMahasiswa
import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.model.domain.Tim
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface DataTimRepository {

    suspend fun insertDataTim(dataTim: DataTim) : Flow<String>

    suspend fun insertTim(tim: Tim) : Flow<String>

    suspend fun getDataTim(idTim: String) : Flow<Result<DataTimEntity>>

    suspend fun deleteAllDataTim() : Flow<String>

    suspend fun getDataTimWithMahasiswa(idTim: String) : Flow<Result<DataTimWithMahasiswa>>

    suspend fun getDataTimWithAll(idTim: String) : Flow<Result<DataTimWithAll>>

}