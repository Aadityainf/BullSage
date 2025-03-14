package com.bullsage.android.data.network

import com.bullsage.android.data.model.AuthRequest
import com.bullsage.android.data.model.NewsResponse
import com.bullsage.android.data.model.RecentMovementResponse
import com.bullsage.android.data.model.SearchResponse
import com.bullsage.android.data.model.StockInfoResponse
import com.bullsage.android.data.model.StockPriceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BullsageApi {
    @POST("/auth/login")
    suspend fun login(
        @Body authRequest: AuthRequest
    )

    @POST("/auth/signup")
    suspend fun signup(
        @Body authRequest: AuthRequest
    )

    @GET("/stock/recent-movements")
    suspend fun getRecentMovements(): RecentMovementResponse

    @GET("/stock/news")
    suspend fun getNews(): NewsResponse

    @GET("/stock/search")
    suspend fun searchStocks(
        @Query("q") searchQuery: String
    ): SearchResponse

    @GET("/stock/{name}/price")
    suspend fun getStockPrice(
        @Path("name") name: String
    ): Response<StockPriceResponse>

    @GET("/stock/{name}/info")
    suspend fun getStockInfo(
        @Path("name") name: String
    ): Response<StockInfoResponse>
}