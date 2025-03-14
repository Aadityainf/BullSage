package com.bullsage.android.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton

class WatchlistRepository {
    private val _watchlist = MutableStateFlow(
        listOf(
            WatchlistItem(name = "Tesla, Inc.", symbol = "TSLA"),
            WatchlistItem(name = "NVIDIA Corporation", symbol = "NVDA")
        )
    )
    val watchlist = _watchlist.asStateFlow()

    fun add(name: String, symbol: String) {
        val item = WatchlistItem(name, symbol)
        _watchlist.update {
            it.toMutableList().apply { add(item) }
        }
    }

    fun delete(symbol: String) {
        _watchlist.update {
            it.toMutableList().apply { removeIf { item -> item.symbol == symbol } }
        }
    }

    fun removeAll() {
        _watchlist.update {
            it.toMutableList().apply { removeAll(this) }
        }
    }

    fun isPresent(symbol: String): Boolean {
        return _watchlist.value.find { it.symbol == symbol } != null
    }
}

data class WatchlistItem(
    val name: String,
    val symbol: String
)