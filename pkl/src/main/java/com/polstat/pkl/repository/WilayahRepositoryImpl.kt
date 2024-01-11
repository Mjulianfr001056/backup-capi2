package com.polstat.pkl.repository

import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.mapper.toWilayahEntity
import com.polstat.pkl.model.domain.Wilayah
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WilayahRepositoryImpl @Inject constructor(
    private val capi63Database: Capi63Database
) : WilayahRepository {

    companion object {
        private const val TAG = "WilayahRepoImpl"
    }

    override suspend fun insertWilayah(
        wilayah: Wilayah,
        nim: String
    ): Flow<String> {
        return  flow {
            try {
                capi63Database.capi63Dao.insertWilayah(wilayah.toWilayahEntity(nim))
                val message = "Berhasil menambahkan wilayah!"
                Log.d(TAG, "insertWilayah: ${message}")
            } catch (e: Exception) {
                val message = "Gagal menambahkan wilayah!"
                Log.d(TAG, "insertWilayah: ${message} (${e.message})")
            }
        }
    }

    override suspend fun getWilayahWithRuta(
        noBS: String
    ): Flow<Result<WilayahWithRuta>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val wilayahWithRuta = capi63Database.capi63Dao.getWilayahWithRuta(noBS)

                Log.d(TAG, "Berhasil getWilayahWithRuta: $wilayahWithRuta")

                emit(Result.Success(wilayahWithRuta))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getWilayahWithRuta: ${e.message}")
                emit(Result.Error(null, "Error fetching WilayahWithRuta: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

}