package com.bullsage.android.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WatchlistResponse(
    val data: List<NetworkWatchlistModel>
)
