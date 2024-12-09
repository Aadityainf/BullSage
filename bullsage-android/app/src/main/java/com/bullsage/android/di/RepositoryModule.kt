package com.bullsage.android.di

import com.bullsage.android.data.repository.AuthRepository
import com.bullsage.android.data.repository.StockRepository
import com.bullsage.android.data.repository.impl.AuthRepositoryImpl
import com.bullsage.android.data.repository.impl.StockRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindStockRepository(impl: StockRepositoryImpl): StockRepository
}