package com.bullsage.android.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bullsage.android.data.model.UserAuthDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAuthManager @Inject constructor(
    private val preferenceDataStore: DataStore<Preferences>
) {
    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    suspend fun getAuthDetails(): UserAuthDetails? {
        return preferenceDataStore.data
            .map { preferences ->
                val token = preferences[USER_TOKEN]
                val email = preferences[USER_EMAIL]

                if (token == null || email == null) {
                    null
                } else {
                    UserAuthDetails(
                        token = token,
                        email = email
                    )
                }
            }
            .first()
    }

    suspend fun saveAuthDetails(userAuthDetails: UserAuthDetails) {
        preferenceDataStore.edit { preferences ->
            preferences[USER_TOKEN] = userAuthDetails.token
            preferences[USER_EMAIL] = userAuthDetails.email
        }
    }

    suspend fun deleteAuthDetails() {
        preferenceDataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
            preferences.remove(USER_EMAIL)
        }
    }
}