package com.autoservice.mobile.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.autoservice.mobile.data.remote.api.SupabaseApi
import com.autoservice.mobile.domain.model.User
import com.autoservice.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking

// Расширьте dataModule, чтобы внедрить Context
// single { androidContext() }

// Для DataStore
private const val USER_PREFERENCES = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES)

// Ключи для хранения данных
private val USER_ID_KEY = stringPreferencesKey("user_id")
private val USERNAME_KEY = stringPreferencesKey("username")
private val EMAIL_KEY = stringPreferencesKey("email")
private val PHONE_KEY = stringPreferencesKey("phone")
private val ROLE_KEY = stringPreferencesKey("role")

class AuthRepositoryImpl(
    private val supabaseApi: SupabaseApi,
    private val context: Context
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(value = null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        restoreUser()
    }

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val response = supabaseApi.getUserByUsername("eq.$username")

            if (response.isSuccessful && response.body() != null) {
                val users = response.body()!!
                val userDto = users.find { it.username == username }

                if (userDto != null) {
                    if (userDto.password == password) {
                        val user = userDto.toDomain()
                        setCurrentUser(user)
                        Result.success(user)
                    } else {
                        Result.failure(Exception("Неверный пароль"))
                    }
                } else {
                    Result.failure(Exception("Пользователь не найден"))
                }
            } else {
                Result.failure(Exception("Ошибка сети: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? {
        return _currentUser.value
    }

    private suspend fun setCurrentUser(user: User?) {
        _currentUser.update { user }
        if (user != null) {
            context.dataStore.edit { preferences ->
                preferences[USER_ID_KEY] = user.id.toString()
                preferences[USERNAME_KEY] = user.username
                preferences[EMAIL_KEY] = user.email ?: ""
                preferences[PHONE_KEY] = user.phone ?: ""
                preferences[ROLE_KEY] = user.role
            }
        } else {
            clearUserData()
        }
    }

    private fun restoreUser() {
        runBlocking {
            try {
                val userData = context.dataStore.data.firstOrNull() ?: emptyPreferences()
                val idString = userData[USER_ID_KEY]
                val username = userData[USERNAME_KEY]

                if (idString != null && username != null) {
                    val id = idString.toIntOrNull()
                    if (id != null) {
                        val user = User(
                            id = id,
                            username = username,
                            email = userData[EMAIL_KEY],
                            phone = userData[PHONE_KEY],
                            role = userData[ROLE_KEY] ?: "user"
                        )
                        _currentUser.value = user
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _currentUser.value = null
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        _currentUser.update { null }
        clearUserData()
        return Result.success(Unit)
    }

    private suspend fun clearUserData() {
        context.dataStore.edit { it.clear() }
    }

    // Ваш метод getCurrentUserId можно оставить, но он теперь избыточен
    // fun getCurrentUserId(): Int? = _currentUser.value?.id
}