package com.polstat.pkl.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.polstat.pkl.database.Capi63Database
import com.polstat.pkl.database.relation.MahasiswaWithAll
import com.polstat.pkl.database.relation.MahasiswaWithWilayah
import com.polstat.pkl.database.relation.WilayahWithRuta
import com.polstat.pkl.mapper.toMahasiswaEntity
import com.polstat.pkl.model.domain.Mahasiswa
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MahasiswaRepositoryImpl @Inject constructor (
    private val capi63Database: Capi63Database
) : MahasiswaRepository {

    companion object {
        private const val TAG = "CAPI63_MHSREPOIMPL"
    }

    override suspend fun insertMahasiswa(
        mahasiswa: Mahasiswa
    ): Flow<String> {
        return flow {
            try {
                Log.d(TAG, "Mahasiswa entity: ${mahasiswa.toMahasiswaEntity()}")
                capi63Database.capi63Dao.insertMahasiswa(mahasiswa.toMahasiswaEntity())
                val message = "Berhasil menambahkan mahasiswa!"
                Log.d(TAG, "insertDataTim: $message $mahasiswa")
                emit(message)
            } catch (e: SQLiteConstraintException) {
                val message = "Gagal menambahkan mahasiswa! Constraint pelanggaran: ${e.message}"
                Log.d(TAG, "insertDataTim: $message")
                emit(message)
            } catch (e: Exception) {
                val message = "Gagal menambahkan mahasiswa! Kesalahan umum: ${e.message}"
                Log.d(TAG, "insertDataTim: $message")
                emit(message)
            }
        }
    }


    override suspend fun getMahasiswaWithWilayah(
        nim: String
    ): Flow<Result<MahasiswaWithWilayah>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val mahasiswaWithWilayah = capi63Database.capi63Dao.getMahasiswaWithWilayah(nim)

                Log.d(TAG, "Berhasil getMahasiswaWithWilayah: $mahasiswaWithWilayah")

                emit(Result.Success(mahasiswaWithWilayah))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getMahasiswaWithWilayah: ${e.message}")
                emit(Result.Error(null, "Error fetching DataTim: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }
    }

    override suspend fun getMahasiswaWithAll(nim: String): Flow<Result<MahasiswaWithAll>> {
        return flow {
            try {
                emit(Result.Loading(true))

                val mahasiswaWithWilayah = capi63Database.capi63Dao.getMahasiswaWithWilayah(nim)

                val listWilayahWithRuta = mahasiswaWithWilayah.listWilayah?.map { wilayah ->

                    val ruta = capi63Database.capi63Dao.getWilayahWithRuta(wilayah.noBS)

                    WilayahWithRuta(wilayah, ruta.listRuta)

                }

                val mahasiswaWithAll = MahasiswaWithAll(
                    mahasiswaWithWilayah = mahasiswaWithWilayah,
                    listWilayahWithRuta = listWilayahWithRuta
                )

                Log.d(TAG, "Berhasil getMahasiswaWithAll: $mahasiswaWithAll")

                emit(Result.Success(mahasiswaWithAll))
            } catch (e: Exception) {
                Log.d(TAG, "Gagal getMahasiswaWithWilayah: ${e.message}")
                emit(Result.Error(null, "Error fetching DataTim: ${e.message}"))
            } finally {
                emit(Result.Loading(false))
            }
        }

    }

}