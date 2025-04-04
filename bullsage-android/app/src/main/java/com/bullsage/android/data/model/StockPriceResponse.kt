package com.bullsage.android.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StockPriceResponse(
    val close: List<Float>,
    val dates: List<String>,
    val predicted: Float
)
