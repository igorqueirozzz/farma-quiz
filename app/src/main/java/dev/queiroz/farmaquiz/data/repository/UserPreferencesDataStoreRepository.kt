package dev.queiroz.farmaquiz.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.queiroz.farmaquiz.data.repository.UserPreferencesDataStoreRepository.PreferenceKeys.IS_FIRST_LAUNCH
import dev.queiroz.farmaquiz.data.repository.UserPreferencesDataStoreRepository.PreferenceKeys.THEME_MODE
import dev.queiroz.farmaquiz.data.repository.UserPreferencesDataStoreRepository.PreferenceKeys.USER_NAME
import dev.queiroz.farmaquiz.model.ThemeMode
import dev.queiroz.farmaquiz.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesDataStoreRepository(private val dataStore: DataStore<Preferences>) {



    private val TAG = "UserPreferencesRepo"

    private object PreferenceKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore
        .data
        .catch {
            if (it is IOException) {
                Log.i(TAG, it.message ?: "No message")
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            mapUserPreferences(it)
        }

    suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userPreferences.userName
            preferences[THEME_MODE] = userPreferences.themeMode.toString()
            preferences[IS_FIRST_LAUNCH] = userPreferences.isFirstLaunch
        }
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val userName: String = preferences[USER_NAME] ?: ""
        val themeMode: ThemeMode =
            ThemeMode.valueOf(preferences[THEME_MODE] ?: ThemeMode.AUTO.toString())
        val isFirstLaunch = preferences[IS_FIRST_LAUNCH] ?: true
        return UserPreferences(userName = userName, themeMode = themeMode, isFirstLaunch = isFirstLaunch)
    }

}