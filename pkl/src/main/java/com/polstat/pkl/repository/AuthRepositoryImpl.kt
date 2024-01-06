package com.polstat.pkl.repository

import com.polstat.pkl.network.AuthApi
import com.polstat.pkl.model.response.AuthResponse
import com.polstat.pkl.model.domain.Session
import com.polstat.pkl.model.domain.User
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionRepository: SessionRepository
) : AuthRepository {
    val session get() = sessionRepository.getActiveSession()

    private fun saveSession(session: Session) {
        sessionRepository.saveSession(session)
    }

    override suspend fun login(nim: String, password: String): Flow<Result<AuthResponse>> {
        return flow {
            emit(Result.Loading(true))
            val authResponse = try {
                val response = authApi.login(nim, password)
                val session = Session(
                    user = User(
                        nama = response.nama,
                        nim = response.nim,
                        avatar = response.avatar,
                        isKoor = response.isKoor,
                        id_kuesioner = response.id_kuesioner
                    ),
                    dataTim = response.dataTim,
                    wilayah = response.wilayah
                )
                saveSession(session)
                response
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error(message = "Authentication Error"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Result.Error(message = "Authentication Error"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = "Authentication Error"))
                return@flow
            }

            emit(Result.Success(authResponse))
            emit(Result.Loading(false))
        }
    }

}