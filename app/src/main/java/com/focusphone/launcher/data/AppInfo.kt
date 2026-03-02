package com.focusphone.launcher.data

import android.graphics.drawable.Drawable

/**
 * Core data models — pure data, no network dependencies.
 */

data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable?,
    val isPinned: Boolean = false,
    val hasMindfulDelay: Boolean = false,
    val isFocusAllowed: Boolean = true   // Allowed during Focus Mode
)

data class DailyIntention(
    val text: String,
    val dateStamp: String  // "yyyy-MM-dd" — used to show once per day
)

data class FocusSession(
    val isActive: Boolean = false,
    val durationMinutes: Int = 25,
    val remainingSeconds: Int = 0,
    val allowedPackages: Set<String> = emptySet()
)

sealed class Screen {
    object Home : Screen()
    object AppDrawer : Screen()
    object Settings : Screen()
    object Onboarding : Screen()
    data class MindfulDelay(
        val packageName: String,
        val appName: String,
        val delaySeconds: Int = 5
    ) : Screen()
    object FocusMode : Screen()
    object PinApps : Screen()
    object ManageMindfulDelay : Screen()
}
