package com.example.travenor_customerapp.core.persistence


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

class OnboardingPrefs(private val context: Context) {

    private val HAS_SEEN = booleanPreferencesKey("has_seen_onboarding")

    val hasSeenOnboarding: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[HAS_SEEN] ?: false }

    suspend fun setHasSeenOnboarding(seen: Boolean) {
        context.dataStore.edit { prefs -> prefs[HAS_SEEN] = seen }

    }
}
