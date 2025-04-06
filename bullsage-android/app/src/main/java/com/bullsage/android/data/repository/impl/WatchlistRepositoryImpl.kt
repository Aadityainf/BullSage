package com.bullsage.android.data.repository.impl

import com.bullsage.android.data.db.WatchlistDao
import com.bullsage.android.data.db.WatchlistEntity
import com.bullsage.android.data.model.NetworkWatchlistModel
import com.bullsage.android.data.model.WatchlistAddRequest
import com.bullsage.android.data.model.WatchlistDeleteRequest
import com.bullsage.android.data.network.BullsageApi
import com.bullsage.android.data.repository.WatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepositoryImpl @Inject constructor(
    private val api: BullsageApi,
    private val watchlistDao: WatchlistDao
) : WatchlistRepository {
    override fun getWatchlistItems(): Flow<List<WatchlistEntity>> {
        return watchlistDao.getWatchlistItems()
    }

    override suspend fun saveFromNetwork() {
        val items = api.getUserWatchlist()
        items.data.forEach {
            watchlistDao.insertIntoWatchlist(
                WatchlistEntity(ticker = it.ticker, longName = it.name)
            )
        }
    }

    override suspend fun addToWatchlist(ticker: String, name: String) {
        api.addToWatchlist(WatchlistAddRequest(NetworkWatchlistModel(ticker = ticker, name = name)))
        watchlistDao.insertIntoWatchlist(WatchlistEntity(ticker = ticker, longName = name))
    }

    override suspend fun removeFromWatchlist(ticker: String) {
        api.deleteFromWatchlist(WatchlistDeleteRequest(ticker))
        watchlistDao.deleteFromWatchlist(ticker)
    }

    override suspend fun isTickerPresent(ticker: String): Boolean {
        return watchlistDao.isPresent(ticker)
    }
}