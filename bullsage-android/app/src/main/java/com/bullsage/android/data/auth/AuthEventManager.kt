package com.bullsage.android.data.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthEventManager @Inject constructor() {
    private val _authEvent = MutableSharedFlow<Boolean>()
    val authEvent: SharedFlow<Boolean> = _authEvent.asSharedFlow()

    suspend fun emitAuthEvent(event: Boolean) {
        _authEvent.emit(event)
    }
}