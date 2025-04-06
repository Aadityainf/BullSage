package com.bullsage.android.data.network

import com.bullsage.android.data.model.AuthRequest
import com.bullsage.android.data.model.AuthResponse
import com.bullsage.android.data.model.NewsResponse
import com.bullsage.android.data.model.RecentMovementResponse
import com.bullsage.android.data.model.SearchResponse
import com.bullsage.android.data.model.StockInfoResponse
import com.bullsage.android.data.model.StockPriceResponse
import com.bullsage.android.data.model.WatchlistAddRequest
import com.bullsage.android.data.model.WatchlistDeleteRequest
import com.bullsage.android.data.model.WatchlistResponse
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
    ): AuthResponse

    @POST("/auth/signup")
    suspend fun signup(
        @Body authRequest: AuthRequest
    ): AuthResponse

    @POST("/auth/logout")
    suspend fun logout()

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

    @GET("/stock/watchlist")
    suspend fun getUserWatchlist(): WatchlistResponse

    @POST("/stock/watchlist/add")
    suspend fun addToWatchlist(
        @Body item: WatchlistAddRequest
    )

    @POST("/stock/watchlist/delete")
    suspend fun deleteFromWatchlist(
        @Body deleteRequest: WatchlistDeleteRequest
    )
}