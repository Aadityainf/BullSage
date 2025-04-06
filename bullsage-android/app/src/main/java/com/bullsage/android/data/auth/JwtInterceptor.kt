package com.bullsage.android.data.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtInterceptor @Inject constructor(
    private val userAuthManager: UserAuthManager,
    private val authEventManager: AuthEventManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        if (url.contains("/login") || url.contains("/signup")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { userAuthManager.getAuthDetails()?.token }

        val newRequest = if (token.isNullOrEmpty()) {
            originalRequest
        } else {
            originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        val response = chain.proceed(newRequest)
        if (response.code == 401) {
            runBlocking {
                userAuthManager.deleteAuthDetails()
                authEventManager.emitAuthEvent(false)
            }
        }

        return response
    }
}