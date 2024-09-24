package com.bullsage.android.data.auth

interface TokenManager {
    suspend fun getToken(): String?

    suspend fun saveToken(token: String)

    suspend fun deleteToken()
}