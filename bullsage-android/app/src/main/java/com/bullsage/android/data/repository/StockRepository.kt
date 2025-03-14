package com.bullsage.android.data.repository

import com.bullsage.android.data.model.Result
import com.bullsage.android.data.model.StockResponse

interface StockRepository {
    suspend fun getRecentMovements(): Result<List<StockResponse>>

    suspend fun searchStock(searchQuery: String): Result<List<String>>
}