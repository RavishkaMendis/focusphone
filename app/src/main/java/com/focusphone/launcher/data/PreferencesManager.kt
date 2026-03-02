package com.focusphone.launcher.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * PreferencesManager
 *
 * The ONLY persistence layer in FocusPhone.
 * Uses Android DataStore — stores everything locally on the device.
 * No network calls. No cloud sync. No accounts.
 *
 * What's stored:
 *   - Pinned app package names
 *   - Mindful delay app package names + delay duration
 *   - Focus mode allowed apps
 *   - Today's daily intention text + date
 *   - Whether onboarding has been completed
 *   - Mindful delay duration setting
 */

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "focusphone_prefs"
)

class PreferencesManager(private val context: Context) {

    companion object {
        private val KEY_PINNED_APPS       = stringSetPreferencesKey("pinned_apps")
        private val KEY_MINDFUL_APPS      = stringSetPreferencesKey("mindful_delay_apps")
        private val KEY_FOCUS_ALLOWED     = stringSetPreferencesKey("focus_allowed_apps")
        private val KEY_INTENTION_TEXT    = stringPreferencesKey("daily_intention_text")
        private val KEY_INTENTION_DATE    = stringPreferencesKey("daily_intention_date")
        private val KEY_ONBOARDING_DONE   = booleanPreferencesKey("onboarding_complete")
        private val KEY_MINDFUL_SECONDS   = intPreferencesKey("mindful_delay_seconds")
        private val KEY_FOCUS_ACTIVE      = booleanPreferencesKey("focus_mode_active")
        private val KEY_FOCUS_DURATION    = intPreferencesKey("focus_duration_minutes")
        private val KEY_SHOW_SECONDS      = booleanPreferencesKey("clock_show_seconds")
    }

    // ── Reads ─────────────────────────────────────────────────────────────────

    val pinnedApps: Flow<Set<String>> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_PINNED_APPS] ?: emptySet() }

    val mindfulDelayApps: Flow<Set<String>> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_MINDFUL_APPS] ?: emptySet() }

    val focusAllowedApps: Flow<Set<String>> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_FOCUS_ALLOWED] ?: emptySet() }

    val dailyIntention: Flow<DailyIntention> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map {
            DailyIntention(
                text = it[KEY_INTENTION_TEXT] ?: "",
                dateStamp = it[KEY_INTENTION_DATE] ?: ""
            )
        }

    val onboardingComplete: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_ONBOARDING_DONE] ?: false }

    val mindfulDelaySeconds: Flow<Int> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_MINDFUL_SECONDS] ?: 5 }

    val focusModeActive: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_FOCUS_ACTIVE] ?: false }

    val focusDurationMinutes: Flow<Int> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_FOCUS_DURATION] ?: 25 }

    val showClockSeconds: Flow<Boolean> = context.dataStore.data
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { it[KEY_SHOW_SECONDS] ?: false }

    // ── Writes ────────────────────────────────────────────────────────────────

    suspend fun togglePinnedApp(packageName: String, pinned: Boolean) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_PINNED_APPS]?.toMutableSet() ?: mutableSetOf()
            if (pinned) current.add(packageName) else current.remove(packageName)
            prefs[KEY_PINNED_APPS] = current
        }
    }

    suspend fun toggleMindfulDelay(packageName: String, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_MINDFUL_APPS]?.toMutableSet() ?: mutableSetOf()
            if (enabled) current.add(packageName) else current.remove(packageName)
            prefs[KEY_MINDFUL_APPS] = current
        }
    }

    suspend fun setFocusAllowedApp(packageName: String, allowed: Boolean) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_FOCUS_ALLOWED]?.toMutableSet() ?: mutableSetOf()
            if (allowed) current.add(packageName) else current.remove(packageName)
            prefs[KEY_FOCUS_ALLOWED] = current
        }
    }

    suspend fun setDailyIntention(text: String, date: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_INTENTION_TEXT] = text
            prefs[KEY_INTENTION_DATE] = date
        }
    }

    suspend fun setOnboardingComplete() {
        context.dataStore.edit { it[KEY_ONBOARDING_DONE] = true }
    }

    suspend fun setMindfulDelaySeconds(seconds: Int) {
        context.dataStore.edit { it[KEY_MINDFUL_SECONDS] = seconds.coerceIn(3, 30) }
    }

    suspend fun setFocusModeActive(active: Boolean) {
        context.dataStore.edit { it[KEY_FOCUS_ACTIVE] = active }
    }

    suspend fun setFocusDuration(minutes: Int) {
        context.dataStore.edit { it[KEY_FOCUS_DURATION] = minutes.coerceIn(5, 120) }
    }

    suspend fun setShowClockSeconds(show: Boolean) {
        context.dataStore.edit { it[KEY_SHOW_SECONDS] = show }
    }
}
