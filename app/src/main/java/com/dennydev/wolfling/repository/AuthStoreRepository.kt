package com.dennydev.wolfling.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "auth")
@ViewModelScoped
class AuthStoreRepository @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    val flowToken: Flow<String> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[tokenKey] ?: ""
        }

    val flowGoogle: Flow<Boolean> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[isGoogle] ?: false
        }

    val flowExpired: Flow<Int> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[expiredKey] ?: 0
        }

    val flowUsername: Flow<String> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[username] ?: ""
        }

    val flowUserId: Flow<String> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[userId] ?: ""
        }

    suspend fun saveToken(token: String, exp: Int, google: Boolean, _username: String, _userId: String) {
        dataStore.edit { settings ->
            settings[tokenKey] = token
            settings[expiredKey] = exp
            settings[isGoogle] = google
            settings[username] = _username
            settings[userId] = _userId
        }
    }

    suspend fun changeUsername(newUsername: String){
        dataStore.edit { settings ->
            settings[username] = newUsername
        }
    }

    suspend fun removeToken(){
        dataStore.edit {
            it[tokenKey] = ""
            it[expiredKey] = 0
            it[isGoogle] = false
            it[username] = ""
            it[userId] = ""
        }
    }

    companion object PreferencesKey{
        val tokenKey = stringPreferencesKey("_token")
        val expiredKey = intPreferencesKey("_exp")
        val isGoogle = booleanPreferencesKey("_google")
        val username = stringPreferencesKey("_username")
        val userId = stringPreferencesKey("_userId")
    }
}