package com.bullsage.android.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "watchlist"
)
data class WatchlistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ticker: String,
    val longName: String
)
