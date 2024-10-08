package com.bullsage.android.data.repository.impl

import com.bullsage.android.data.auth.TokenManager
import com.bullsage.android.data.model.Result
import com.bullsage.android.data.repository.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager
) : AuthRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            // simulate network call
            delay(1000)

            tokenManager.saveToken("hello")

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            // simulate network call
            delay(1000)

            tokenManager.saveToken("hello")

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            tokenManager.deleteToken()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }
}