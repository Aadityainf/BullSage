package com.bullsage.android.data.network

import com.bullsage.android.data.model.AuthRequest
import com.bullsage.android.data.model.RecentMovementResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JetxApi {
    @POST("/login")
    suspend fun login(
        @Body authRequest: AuthRequest
    )

    @POST("/signup")
    suspend fun signup(
        @Body authRequest: AuthRequest
    )

    @GET("/stocks/recent-movements")
    suspend fun getRecentMovements(): RecentMovementResponse
}