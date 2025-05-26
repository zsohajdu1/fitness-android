package aut.bme.hu.fitness.auth.service.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import aut.bme.hu.fitness.api.UnauthenticatedApiService
import aut.bme.hu.fitness.auth.request.LoginRequest
import aut.bme.hu.fitness.auth.request.RegisterRequest
import aut.bme.hu.fitness.auth.service.AuthService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val api: UnauthenticatedApiService, @ApplicationContext context: Context
) : AuthService {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val HAS_USER = booleanPreferencesKey("has_user")
    }

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.dataStoreFile("auth_prefs.preferences_pb")
    }

    private var hasUserPrivate: Boolean = false
    private var accessToken: String = ""
    private var initialized = false

    override val hasUser: Boolean
        get() = initialized && hasUserPrivate && accessToken.isNotEmpty()

    override suspend fun signUp(email: String, password: String) {
        initialize()
        val token = api.register(RegisterRequest(email, password)).accessToken
        updateSession(token)
    }

    override suspend fun authenticate(email: String, password: String) {
        initialize()
        val token = api.login(LoginRequest(email, password)).accessToken
        updateSession(token)
    }

    override suspend fun signOut() {
        initialize()
        hasUserPrivate = false
        accessToken = ""
        dataStore.edit {
            it[HAS_USER] = false
            it[ACCESS_TOKEN] = ""
        }
    }

    override fun getAccessToken(): String = accessToken

    override suspend fun initialize() {
        if (initialized) return
        try {
            val prefs = dataStore.data.first()
            hasUserPrivate = prefs[HAS_USER] ?: false
            accessToken = prefs[ACCESS_TOKEN] ?: ""
            if (!api.validate(accessToken)) {
                hasUserPrivate = false
                accessToken = ""
            }
            initialized = true
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun updateSession(token: String) {
        hasUserPrivate = true
        accessToken = token
        dataStore.edit {
            it[HAS_USER] = true
            it[ACCESS_TOKEN] = token
        }
    }
}
