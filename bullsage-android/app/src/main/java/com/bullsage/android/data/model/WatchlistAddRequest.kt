package com.bullsage.android.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WatchlistAddRequest(
    val ticker: NetworkWatchlistModel
)

@JsonClass(generateAdapter = true)
data class NetworkWatchlistModel(
    val ticker: String,
    val name: String
)