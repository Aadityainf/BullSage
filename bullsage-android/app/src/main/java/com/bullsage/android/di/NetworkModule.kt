package com.bullsage.android.di

import com.bullsage.android.BuildConfig
import com.bullsage.android.data.auth.JwtInterceptor
import com.bullsage.android.data.network.BullsageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideJetxApi(
        jwtInterceptor: JwtInterceptor
    ): BullsageApi {
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(jwtInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(BullsageApi::class.java)
    }
}