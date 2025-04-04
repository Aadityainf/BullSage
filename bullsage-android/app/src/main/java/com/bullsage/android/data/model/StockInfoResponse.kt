package com.bullsage.android.data.model

import com.squareup.moshi.JsonClass
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class StockInfoResponse(
    val averageVolume : String,
    val beta: String,
    val currency: String,
    val currentPrice: Float,
    val dividendRate: String?,
    val dividendYield: String?,
    val earningsTimestampEnd: Long,
    val earningsTimestampStart: Long,
    val longName: String,
    val marketCap: String,
    val open: String,
    val previousClose: String,
    val targetMeanPrice: String,
    val trailingPE: String,
    val volume: String
) {
    val earnings = "${getDateFromTimestamp(earningsTimestampStart)} - ${getDateFromTimestamp(earningsTimestampEnd)}"

    val forwardDividendAndYield = if (dividendRate == null || dividendYield == null) "--" else "$dividendRate (${dividendYield}%)"

    private fun getDateFromTimestamp(timeStamp: Long): String {
        val instant = Instant.ofEpochSecond(timeStamp)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        return formatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()))
    }
}
