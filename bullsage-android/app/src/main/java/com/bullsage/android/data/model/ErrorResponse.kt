package com.bullsage.android.data.model

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.HttpException

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val message: String
)

fun HttpException.getErrorMessage(): String? {
    val errorJson = this.response()?.errorBody()?.source()
    val moshi = Moshi.Builder().build()
    val jsonAdapter = moshi.adapter(ErrorResponse::class.java).lenient()
    return errorJson?.let { jsonAdapter.fromJson(it)?.message }
}