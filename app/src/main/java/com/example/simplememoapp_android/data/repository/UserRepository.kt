// data/repository/UserRepository.kt (全文)
package com.example.simplememoapp_android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.simplememoapp_android.network.ApiService
import com.example.simplememoapp_android.network.dto.UserRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val LOGGED_IN_USER_ID = stringPreferencesKey("logged_in_user_id")
    }

    val loggedInUserIdFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.LOGGED_IN_USER_ID]
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOGGED_IN_USER_ID] = userId
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val users = apiService.getUserByEmail(email)
            val user = users.firstOrNull()
            if (user != null) {
                // MockAPIではパスワード検証ができないため、Emailの存在だけで成功とみなす
                saveUserId(user.id)
                Result.success(user.id)
            } else {
                Result.failure(Exception("ユーザーが見つかりません"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<String> {
        return try {
            val request = UserRequest(email = email, password = password)
            val user = apiService.registerUser(request)
            saveUserId(user.id)
            Result.success(user.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}