package com.bullsage.android.data.repository

import com.bullsage.android.data.db.WatchlistEntity
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    fun getWatchlistItems(): Flow<List<WatchlistEntity>>

    suspend fun saveFromNetwork()

    suspend fun addToWatchlist(ticker: String, name: String)

    suspend fun removeFromWatchlist(ticker: String)

    suspend fun isTickerPresent(ticker: String): Boolean
}