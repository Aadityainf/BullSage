package com.bullsage.android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WatchlistEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BullsageDatabase: RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
}