package com.polstat.pkl.auth.repository

import com.polstat.pkl.auth.response.AuthResponse
import com.polstat.pkl.utils.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(nim: String, password: String): Flow<Result<AuthResponse>>
}