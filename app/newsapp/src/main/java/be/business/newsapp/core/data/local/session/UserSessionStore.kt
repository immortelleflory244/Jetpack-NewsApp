package be.business.newsapp.core.data.local.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun currentUserId(): Flow<Long?> = dataStore.data.map { it[CURRENT_USER_ID] }

    suspend fun setCurrentUserId(userId: Long) {
        dataStore.edit { it[CURRENT_USER_ID] = userId }
    }

    suspend fun clearSession() {
        dataStore.edit { it.remove(CURRENT_USER_ID) }
    }

    private companion object {
        val CURRENT_USER_ID = longPreferencesKey("current_user_id")
    }
}
