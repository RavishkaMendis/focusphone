package com.focusphone.launcher.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.*
import com.focusphone.launcher.data.AppInfo
import com.focusphone.launcher.ui.components.*
import com.focusphone.launcher.ui.theme.*
import com.focusphone.launcher.viewmodel.MainViewModel

/**
 * SettingsScreen
 *
 * Three sections:
 *   1. Home Screen — configure pinned apps
 *   2. Focus — mindful delay, focus mode setup
 *   3. About — privacy, version
 *
 * No account. No sync. No cloud. No data leaving this device.
 */
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSection by remember { mutableStateOf<SettingsSection?>(null) }

    when (currentSection) {
        SettingsSection.PinApps ->
            PinAppsScreen(viewModel = viewModel, onBack = { currentSection = null })
        SettingsSection.MindfulDelay ->
            MindfulDelaySettingsScreen(viewModel = viewModel, onBack = { currentSection = null })
        SettingsSection.FocusMode ->
            FocusModeSetupScreen(viewModel = viewModel, onBack = { currentSection = null })
        null -> SettingsRoot(
            viewModel = viewModel,
            onNavigate = { currentSection = it },
            onBack = onBack,
            modifier = modifier
        )
    }
}

enum class SettingsSection { PinApps, MindfulDelay, FocusMode }

@Composable
private fun SettingsRoot(
    viewModel: MainViewModel,
    onNavigate: (SettingsSection) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showSeconds     by viewModel.showClockSeconds.collectAsState()
    val focusActive     by viewModel.focusModeActive.collectAsState()
    val pinnedCount     by viewModel.pinnedPackages.collectAsState()
    val mindfulCount    by viewModel.mindfulPackages.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineLarge,
                color = White
            )
            Text(
                text = "done",
                style = MaterialTheme.typography.bodyLarge,
                color = White60,
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(8.dp)
            )
        }

        LazyColumn {
            // ── HOME SCREEN ──────────────────────────────────────────────────
            item {
                SettingsSectionHeader("HOME SCREEN")
            }
            item {
                SettingsNavRow(
                    title = "Pinned Apps",
                    subtitle = "Apps shown on your home screen",
                    value = "${pinnedCount.size} pinned",
                    onClick = { onNavigate(SettingsSection.PinApps) }
                )
            }
            item {
                SectionDivider()
            }
            item {
                SettingsToggleRow(
                    title = "Show Seconds",
                    subtitle = "Display seconds in the clock",
                    checked = showSeconds,
                    onCheckedChange = { viewModel.setShowClockSeconds(it) }
                )
            }

            // ── FOCUS ─────────────────────────────────────────────────────────
            item { Spacer(Modifier.height(24.dp)) }
            item { SettingsSectionHeader("FOCUS") }
            item {
                SettingsNavRow(
                    title = "Mindful Delay",
                    subtitle = "Add a pause before opening selected apps",
                    value = "${mindfulCount.size} apps",
                    onClick = { onNavigate(SettingsSection.MindfulDelay) }
                )
            }
            item { SectionDivider() }
            item {
                SettingsNavRow(
                    title = "Focus Mode",
                    subtitle = if (focusActive) "Active — tap to manage" else "Block non-essential apps for a session",
                    onClick = { onNavigate(SettingsSection.FocusMode) }
                )
            }

            // ── ABOUT ─────────────────────────────────────────────────────────
            item { Spacer(Modifier.height(24.dp)) }
            item { SettingsSectionHeader("PRIVACY & ABOUT") }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 18.dp)
                ) {
                    Text("FocusPhone", style = MaterialTheme.typography.bodyLarge, color = White90)
                    Spacer(Modifier.height(2.dp))
                    Text("Version 1.0.0", style = MaterialTheme.typography.bodyMedium, color = White30)
                }
            }
            item { SectionDivider() }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        "Privacy Guarantee",
                        style = MaterialTheme.typography.bodyLarge,
                        color = White90
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "This app requests zero internet permissions. " +
                        "No data is collected, transmitted, or stored outside your device. " +
                        "No analytics. No crash reporters. No ads. " +
                        "Your phone usage stays private — forever.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White30,
                        lineHeight = 22.sp
                    )
                }
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = White30,
        letterSpacing = 2.sp,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
    )
}

// ═══════════════════════════════════════════════════════════════════════
// PIN APPS SCREEN
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun PinAppsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val allApps         by viewModel.allApps.collectAsState()
    val pinnedPackages  by viewModel.pinnedPackages.collectAsState()
    val isLoading       by viewModel.isLoadingApps.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        ScreenHeader(title = "Pin Apps", subtitle = "Tap to pin/unpin from home screen", onBack = onBack)

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("loading...", style = MaterialTheme.typography.bodyMedium, color = White30)
            }
        } else {
            LazyColumn {
                items(allApps, key = { it.packageName }) { app ->
                    val isPinned = app.packageName in pinnedPackages
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.togglePin(app.packageName) }
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = app.appName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isPinned) White else White60
                        )
                        if (isPinned) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(White)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
// MINDFUL DELAY SETTINGS
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun MindfulDelaySettingsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val allApps         by viewModel.allApps.collectAsState()
    val mindfulApps     by viewModel.mindfulPackages.collectAsState()
    val delaySeconds    by viewModel.mindfulDelaySeconds.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        ScreenHeader(
            title = "Mindful Delay",
            subtitle = "A pause before opening selected apps",
            onBack = onBack
        )

        LazyColumn {
            item {
                Column(Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                    Text(
                        "Delay Duration",
                        style = MaterialTheme.typography.bodyLarge,
                        color = White90
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "$delaySeconds seconds",
                        style = MaterialTheme.typography.headlineMedium,
                        color = White
                    )
                    Spacer(Modifier.height(16.dp))
                    Slider(
                        value = delaySeconds.toFloat(),
                        onValueChange = { viewModel.setMindfulDelaySeconds(it.toInt()) },
                        valueRange = 3f..30f,
                        steps = 26,
                        colors = SliderDefaults.colors(
                            thumbColor = White,
                            activeTrackColor = White,
                            inactiveTrackColor = White10
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("3s", style = MaterialTheme.typography.labelSmall, color = White30)
                        Text("30s", style = MaterialTheme.typography.labelSmall, color = White30)
                    }
                }
            }

            item { SectionDivider() }
            item { Spacer(Modifier.height(8.dp)) }
            item {
                Text(
                    text = "SELECT APPS",
                    style = MaterialTheme.typography.labelSmall,
                    color = White30,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            items(allApps, key = { it.packageName }) { app ->
                val hasMindful = app.packageName in mindfulApps
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleMindfulDelay(app.packageName) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (hasMindful) White else White60
                    )
                    if (hasMindful) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(WarningAmber)
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
// FOCUS MODE SETUP
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun FocusModeSetupScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val allApps         by viewModel.allApps.collectAsState()
    val focusAllowed    by viewModel.focusAllowedApps.collectAsState()
    val focusActive     by viewModel.focusModeActive.collectAsState()
    val focusDuration   by viewModel.focusDurationMinutes.collectAsState()
    val focusRemaining  by viewModel.focusSecondsRemaining.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        ScreenHeader(
            title = "Focus Mode",
            subtitle = if (focusActive) "Session active" else "Block all but your essentials",
            onBack = onBack
        )

        LazyColumn {
            if (focusActive) {
                item {
                    Column(Modifier.padding(24.dp)) {
                        val mins = focusRemaining / 60
                        val secs = focusRemaining % 60
                        Text(
                            text = "%02d:%02d".format(mins, secs),
                            style = MaterialTheme.typography.displayMedium,
                            color = FocusBlue
                        )
                        Spacer(Modifier.height(4.dp))
                        Text("remaining", style = MaterialTheme.typography.bodyMedium, color = White30)
                        Spacer(Modifier.height(24.dp))
                        GhostButton(
                            text = "END SESSION",
                            onClick = {
                                viewModel.endFocusMode()
                                onBack()
                            },
                            color = White60
                        )
                    }
                }
            } else {
                item {
                    Column(Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                        Text("Duration", style = MaterialTheme.typography.bodyLarge, color = White90)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "$focusDuration minutes",
                            style = MaterialTheme.typography.headlineMedium,
                            color = White
                        )
                        Spacer(Modifier.height(16.dp))
                        Slider(
                            value = focusDuration.toFloat(),
                            onValueChange = { /* handled in VM */ },
                            onValueChangeFinished = { },
                            valueRange = 5f..120f,
                            colors = SliderDefaults.colors(
                                thumbColor = FocusBlue,
                                activeTrackColor = FocusBlue,
                                inactiveTrackColor = White10
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("5m", style = MaterialTheme.typography.labelSmall, color = White30)
                            Text("120m", style = MaterialTheme.typography.labelSmall, color = White30)
                        }
                        Spacer(Modifier.height(24.dp))
                        SolidButton(
                            text = "START FOCUS",
                            onClick = {
                                viewModel.startFocusMode(focusDuration)
                                onBack()
                            }
                        )
                    }
                }
            }

            item { SectionDivider() }
            item { Spacer(Modifier.height(8.dp)) }
            item {
                Text(
                    text = "ALLOWED APPS DURING FOCUS",
                    style = MaterialTheme.typography.labelSmall,
                    color = White30,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            items(allApps, key = { it.packageName }) { app ->
                val isAllowed = app.packageName in focusAllowed
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleFocusAllowed(app.packageName) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isAllowed) White else White30
                    )
                    if (isAllowed) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(FocusBlue)
                        )
                    }
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════

@Composable
private fun ScreenHeader(
    title: String,
    subtitle: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(top = 20.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.headlineLarge, color = White)
            Text(
                text = "← back",
                style = MaterialTheme.typography.bodyMedium,
                color = White30,
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(8.dp)
            )
        }
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = White30)
    }
}
