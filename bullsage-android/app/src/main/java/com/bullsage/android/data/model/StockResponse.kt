package com.bullsage.android.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StockResponse(
    val symbol: String,
    val name: String,
    @Json(name = "latest_close") val latestClose: Double,
    @Json(name = "percent_change") val percentChange: Double,
)
