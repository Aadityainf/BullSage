package com.bullsage.android.di

import com.bullsage.android.data.auth.UserAuthDataStore
import com.bullsage.android.data.auth.UserAuthManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserAuthManagerModule {
    @Binds
    @Singleton
    abstract fun bindUserAuthManager(tokenDataStore: UserAuthDataStore) : UserAuthManager
}