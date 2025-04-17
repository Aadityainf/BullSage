package com.bullsage.android.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist")
    fun getWatchlistItems(): Flow<List<WatchlistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE ticker = :ticker)")
    suspend fun isPresent(ticker: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoWatchlist(watchlistEntity: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE ticker = :ticker")
    suspend fun deleteFromWatchlist(ticker: String)

    @Query("DELETE FROM watchlist")
    suspend fun emptyWatchlist()
}
