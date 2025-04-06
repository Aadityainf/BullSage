package com.bullsage.android.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsResponse(
    val data: List<NewsItem>
)

@JsonClass(generateAdapter = true)
data class NewsItem(
    val title: String,
    val link: String,
    val thumbnail: ThumbnailItem?
)

@JsonClass(generateAdapter = true)
data class ThumbnailItem(
    val resolutions: List<ThumbnailUrl>
)

@JsonClass(generateAdapter = true)
data class ThumbnailUrl(
    val url: String
)
