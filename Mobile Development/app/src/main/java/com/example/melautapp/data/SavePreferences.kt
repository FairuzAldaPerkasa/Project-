import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pref")

class SavePreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val isLoginKey = booleanPreferencesKey("is_login")
    private val tokenKey = stringPreferencesKey("token")
    private val userId = intPreferencesKey("user_id")

    fun isLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isLoginKey] ?: false
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[tokenKey]
        }
    }
    fun getUserId(): Flow<Int?> {
        return dataStore.data.map { preferences ->
            preferences[userId]
        }
    }

    suspend fun saveLoginStatus(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[isLoginKey] = isLogin
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    suspend fun saveUserId(id: Int) {
        dataStore.edit { preferences ->
            preferences[userId] = id
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(isLoginKey)
            preferences.remove(tokenKey)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SavePreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SavePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SavePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}