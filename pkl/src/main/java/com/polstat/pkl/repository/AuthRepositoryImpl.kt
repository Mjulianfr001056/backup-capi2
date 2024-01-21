package com.polstat.pkl.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.polstat.pkl.model.domain.Session
import com.polstat.pkl.model.response.AuthResponse
import com.polstat.pkl.network.AuthApi
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import java.time.Instant
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionRepository: SessionRepository
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    val session get() = sessionRepository.getActiveSession()

    private fun saveSession(session: Session) {
        sessionRepository.saveSession(session)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun login(nim: String, password: String): Flow<Result<AuthResponse>> {
        return flow {
            emit(Result.Loading(true))
            val authResponse = try {
                val response = authApi.login(nim, password)
                val session = Session(
                    nama = response.nama,
                    nim = response.nim,
                    avatar = response.avatar,
                    isKoor = response.isKoor,
                    id_kuesioner = response.idKuesioner,
                    idTim = response.dataTim.idTim,
                    token = response.token
                )
                saveSession(session)
                sessionRepository.logIn()
                Log.d(TAG, "Session was saved: $session")
                response
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = e.localizedMessage ?: "Authentication Error"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                when (e.code()) {
                    404 -> emit(Result.Error(message = "Nim tidak ditemukan!"))
                    400 -> emit(Result.Error(message = "Password salah!"))
                    else -> emit(Result.Error(message = e.localizedMessage ?: "Authentication Error"))
                }
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = e.localizedMessage ?: "Authentication Error"))
                return@flow
            }

            emit(Result.Success(authResponse))
            emit(Result.Loading(false))
        }
    }

}