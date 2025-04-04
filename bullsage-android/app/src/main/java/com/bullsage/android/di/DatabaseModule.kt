package com.bullsage.android.di

import android.content.Context
import androidx.room.Room
import com.bullsage.android.data.db.BullsageDatabase
import com.bullsage.android.data.db.WatchlistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Singleton
    @Provides
    fun provideBullsageDatabase(
        @ApplicationContext context: Context
    ): BullsageDatabase {
        return Room
            .databaseBuilder(context, BullsageDatabase::class.java, "bullsage.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideWatchlistDao(
        db: BullsageDatabase
    ): WatchlistDao {
        return db.watchlistDao()
    }
}