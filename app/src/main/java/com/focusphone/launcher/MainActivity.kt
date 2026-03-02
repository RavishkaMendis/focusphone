package com.focusphone.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.focusphone.launcher.data.Screen
import com.focusphone.launcher.ui.screens.*
import com.focusphone.launcher.ui.theme.FocusPhoneTheme
import com.focusphone.launcher.viewmodel.LaunchResult
import com.focusphone.launcher.viewmodel.MainViewModel

/**
 * MainActivity
 *
 * The single Activity that hosts all screens.
 * Navigation is state-driven — a simple sealed class Stack.
 * No Navigation component needed for something this focused.
 *
 * Screen flow:
 *   Onboarding (first time) → Home → AppDrawer / Settings
 *                                   ↘ MindfulDelay (overlay)
 */
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FocusPhoneTheme {
                FocusPhoneApp(viewModel = viewModel)
            }
        }
    }

    // Prevent accidental back-press from home
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Intentionally do nothing — home screen cannot be dismissed
    }
}

@Composable
private fun FocusPhoneApp(viewModel: MainViewModel) {
    val onboardingComplete by viewModel.onboardingComplete.collectAsState()
    val mindfulTarget      by viewModel.mindfulDelayTarget.collectAsState()
    val delaySeconds       by viewModel.mindfulDelaySeconds.collectAsState()
    val context            = LocalContext.current

    // Navigation stack — keeps it simple
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    // Wait for initial state load before showing anything
    if (!onboardingComplete && currentScreen == Screen.Home) {
        // Still need to confirm from prefs — onboarding check
        // handled below via animation
    }

    // ── Mindful Delay Overlay ─────────────────────────────────────────────────
    // Floats above everything when triggered
    mindfulTarget?.let { app ->
        MindfulDelayScreen(
            appName = app.appName,
            totalSeconds = delaySeconds,
            onProceed = { viewModel.launchAfterDelay(app.packageName) },
            onCancel  = { viewModel.cancelMindfulDelay() }
        )
        return
    }

    // ── Back Handler ──────────────────────────────────────────────────────────
    BackHandler(enabled = currentScreen != Screen.Home) {
        currentScreen = Screen.Home
    }

    // ── Main Navigation ───────────────────────────────────────────────────────
    AnimatedContent(
        targetState = Pair(onboardingComplete, currentScreen),
        transitionSpec = {
            val entering = targetState.second
            val leaving  = initialState.second

            when {
                // Onboarding → Home: fade
                !initialState.first && targetState.first ->
                    fadeIn(tween(600)) togetherWith fadeOut(tween(400))

                // Home → AppDrawer: slide up
                leaving == Screen.Home && entering == Screen.AppDrawer ->
                    slideInVertically(tween(300)) { it } + fadeIn(tween(300)) togetherWith
                    fadeOut(tween(200))

                // AppDrawer → Home: slide down
                leaving == Screen.AppDrawer && entering == Screen.Home ->
                    fadeIn(tween(200)) togetherWith
                    slideOutVertically(tween(300)) { it } + fadeOut(tween(300))

                // Default: crossfade
                else -> fadeIn(tween(250)) togetherWith fadeOut(tween(200))
            }
        },
        label = "mainNav",
        modifier = Modifier.fillMaxSize()
    ) { (isOnboarded, screen) ->

        if (!isOnboarded) {
            OnboardingScreen(
                onComplete = {
                    viewModel.completeOnboarding()
                    currentScreen = Screen.Home
                }
            )
            return@AnimatedContent
        }

        when (screen) {
            Screen.Home -> HomeScreen(
                viewModel = viewModel,
                onOpenAppDrawer = { currentScreen = Screen.AppDrawer },
                onOpenSettings  = { currentScreen = Screen.Settings },
                modifier = Modifier.fillMaxSize()
            )

            Screen.AppDrawer -> AppDrawerScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.Home },
                modifier = Modifier.fillMaxSize()
            )

            Screen.Settings -> SettingsScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.Home },
                modifier = Modifier.fillMaxSize()
            )

            else -> HomeScreen(
                viewModel = viewModel,
                onOpenAppDrawer = { currentScreen = Screen.AppDrawer },
                onOpenSettings  = { currentScreen = Screen.Settings },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
