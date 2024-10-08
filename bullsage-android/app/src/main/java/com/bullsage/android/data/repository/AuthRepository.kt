package com.bullsage.android.data.repository

import com.bullsage.android.data.model.Result

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String
    ) : Result<Unit>

    suspend fun signUp(
        email: String,
        password: String
    ) : Result<Unit>

    suspend fun signOut() : Result<Unit>
}