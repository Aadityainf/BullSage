package com.bullsage.android.data.auth

import com.bullsage.android.data.model.UserAuthDetails

interface UserAuthManager {
    suspend fun getAuthDetails(): UserAuthDetails?

    suspend fun saveAuthDetails(userAuthDetails: UserAuthDetails)

    suspend fun deleteAuthDetails()
}