package com.bullsage.android.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataStore @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>
) : TokenManager {
    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    override suspend fun getToken(): String? {
        return preferenceDataStore.data
            .map { preferences -> preferences[USER_TOKEN] }
            .first()
    }

    override suspend fun saveToken(token: String) {
        preferenceDataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    override suspend fun deleteToken() {
        preferenceDataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
        }
    }
}