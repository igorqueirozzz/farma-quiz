package dev.queiroz.farmaquiz.data.repository

import dev.queiroz.farmaquiz.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesDataStoreRepository {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun updateUserPreferences(userPreferences: UserPreferences)
}