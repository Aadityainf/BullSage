package com.bullsage.android.di

import com.bullsage.android.data.auth.TokenDataStore
import com.bullsage.android.data.auth.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenManagerModule {
    @Binds
    abstract fun bindTokenManager(
        tokenDataStore: TokenDataStore
    ) : TokenManager
}