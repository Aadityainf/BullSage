package com.bullsage.android.data.repository.impl

import com.bullsage.android.data.model.Result
import com.bullsage.android.data.model.StockResponse
import com.bullsage.android.data.network.JetxApi
import com.bullsage.android.data.repository.StockRepository
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val api: JetxApi
): StockRepository {
    override suspend fun getRecentMovements(): Result<List<StockResponse>> {
        return try {
            val recentMovements = api.getRecentMovements().data
            Result.Success(recentMovements)
        } catch (e: Exception) {
            Result.Error()
        }
    }

    override suspend fun searchStock() {
        // TODO: implement
    }
}