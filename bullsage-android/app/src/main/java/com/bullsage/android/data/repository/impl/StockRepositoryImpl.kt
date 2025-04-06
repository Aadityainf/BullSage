package com.bullsage.android.data.repository.impl

import com.bullsage.android.data.model.Result
import com.bullsage.android.data.model.StockResponse
import com.bullsage.android.data.network.BullsageApi
import com.bullsage.android.data.repository.StockRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: BullsageApi
): StockRepository {
    override suspend fun getRecentMovements(): Result<List<StockResponse>> {
        return try {
            val recentMovements = api.getRecentMovements().data
            Result.Success(recentMovements)
        } catch (_: Exception) {
            Result.Error()
        }
    }

    override suspend fun searchStock(searchQuery: String): Result<List<String>> {
        return try {
            val stockSearchResults = api.searchStocks(searchQuery).data
            Result.Success(stockSearchResults)
        } catch (_: Exception) {
            Result.Error()
        }
    }
}