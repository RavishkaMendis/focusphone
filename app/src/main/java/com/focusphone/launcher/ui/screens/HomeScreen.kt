package com.focusphone.launcher.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.focusphone.launcher.data.AppInfo
import com.focusphone.launcher.ui.components.*
import com.focusphone.launcher.ui.theme.*
import com.focusphone.launcher.viewmodel.*

/**
 * HomeScreen
 *
 * The user's most-seen screen. Every pixel is deliberate.
 *
 * Layout (top → bottom):
 *   ── Status Bar Space
 *   ── Focus Mode Banner (when active)
 *   ── Clock (large, ultra-light)
 *   ── Date
 *   ── Daily Intention (if set)
 *   ── SPACER (push apps down)
 *   ── Pinned Apps (text list)
 *   ── "↑ all apps" pull handle
 *   ── Privacy Badge
 *   ── Nav Bar Space
 *
 * No icons. No color. No noise.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onOpenAppDrawer: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pinnedApps       by viewModel.pinnedApps.collectAsState()
    val currentTime      by viewModel.currentTime.collectAsState()
    val currentDate      by viewModel.currentDate.collectAsState()
    val intentionText    by viewModel.intentionText.collectAsState()
    val showIntention    by viewModel.showIntentionPrompt.collectAsState()
    val focusActive      by viewModel.focusModeActive.collectAsState()
    val focusRemaining   by viewModel.focusSecondsRemaining.collectAsState()
    val focusAllowed     by viewModel.focusAllowedApps.collectAsState()
    val mindfulApps      by viewModel.mindfulPackages.collectAsState()

    var showAppContextMenu by remember { mutableStateOf<AppInfo?>(null) }

    // Daily intention overlay
    if (showIntention) {
        DailyIntentionOverlay(
            onSave = { viewModel.saveIntention(it) },
            onSkip = { viewModel.skipIntention() }
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .combinedClickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = {},
                onLongClick = onOpenSettings
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ── TOP SECTION ───────────────────────────────────────────────────
            Column {
                // Focus mode banner
                AnimatedVisibility(
                    visible = focusActive,
                    enter = fadeIn() + expandVertically(),
                    exit  = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(Modifier.height(12.dp))
                        FocusModeBanner(
                            secondsRemaining = focusRemaining,
                            onEndFocus = { viewModel.endFocusMode() }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(if (focusActive) 8.dp else 48.dp))

                // ── CLOCK ─────────────────────────────────────────────────────
                Text(
                    text = currentTime,
                    style = MaterialTheme.typography.displayLarge,
                    color = White,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(Modifier.height(4.dp))

                // ── DATE ──────────────────────────────────────────────────────
                Text(
                    text = currentDate,
                    style = MaterialTheme.typography.bodyLarge,
                    color = White30,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                // ── DAILY INTENTION ───────────────────────────────────────────
                AnimatedVisibility(
                    visible = intentionText.isNotBlank(),
                    enter = fadeIn(tween(400)) + expandVertically(tween(400)),
                    exit  = fadeOut(tween(200)) + shrinkVertically(tween(200))
                ) {
                    Column {
                        Spacer(Modifier.height(28.dp))
                        IntentionDisplay(
                            text = intentionText,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            }

            // ── BOTTOM SECTION: Pinned Apps ───────────────────────────────────
            Column {
                if (pinnedApps.isEmpty()) {
                    EmptyPinnedAppsHint(
                        onOpenSettings = onOpenSettings,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                } else {
                    // Pinned app list
                    pinnedApps.forEach { app ->
                        AppItem(
                            name = app.appName,
                            isPinned = true,
                            hasMindfulDelay = app.packageName in mindfulApps,
                            isFocusBlocked = focusActive && app.packageName !in focusAllowed,
                            onTap = {
                                val result = viewModel.onAppTap(app)
                                if (result is LaunchResult.MindfulDelayRequired) {
                                    // Handled by overlay in MainActivity
                                }
                            },
                            onLongPress = { showAppContextMenu = app }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── APP DRAWER HANDLE ──────────────────────────────────────────
                AppDrawerHandle(onClick = onOpenAppDrawer)

                Spacer(Modifier.height(12.dp))

                // ── PRIVACY BADGE ──────────────────────────────────────────────
                PrivacyBadge(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(20.dp))
            }
        }

        // ── App Context Menu ──────────────────────────────────────────────────
        showAppContextMenu?.let { app ->
            AppContextMenu(
                app = app,
                isPinned = true,
                hasMindfulDelay = app.packageName in mindfulApps,
                onDismiss = { showAppContextMenu = null },
                onTogglePin = {
                    viewModel.togglePin(app.packageName)
                    showAppContextMenu = null
                },
                onToggleMindful = {
                    viewModel.toggleMindfulDelay(app.packageName)
                    showAppContextMenu = null
                }
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════

@Composable
private fun IntentionDisplay(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(32.dp)
                .background(White30)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = White60,
            maxLines = 2
        )
    }
}

@Composable
private fun AppDrawerHandle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Visual handle pill
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(White30)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "all apps",
            style = MaterialTheme.typography.labelMedium,
            color = White30,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun EmptyPinnedAppsHint(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "No apps pinned.",
            style = MaterialTheme.typography.titleMedium,
            color = White30
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Long press home · Settings · Pin Apps",
            style = MaterialTheme.typography.bodyMedium,
            color = White30.copy(alpha = 0.5f)
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════
// DAILY INTENTION OVERLAY
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun DailyIntentionOverlay(
    onSave: (String) -> Unit,
    onSkip: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Good morning.",
                style = MaterialTheme.typography.displayMedium,
                color = White
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "What's your #1 focus today?",
                style = MaterialTheme.typography.bodyLarge,
                color = White60
            )

            Spacer(Modifier.height(48.dp))

            // Minimal text input
            BasicTextField(
                value = text,
                onValueChange = { if (it.length <= 80) text = it },
                textStyle = MaterialTheme.typography.headlineMedium.copy(color = White),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(White),
                singleLine = false,
                maxLines = 3,
                decorationBox = { inner ->
                    Box {
                        if (text.isEmpty()) {
                            Text(
                                text = "type here...",
                                style = MaterialTheme.typography.headlineMedium,
                                color = White30
                            )
                        }
                        inner()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(if (text.isNotBlank()) White30 else White10)
            )

            Spacer(Modifier.height(48.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SolidButton(
                    text = "SET INTENTION",
                    onClick = { if (text.isNotBlank()) onSave(text.trim()) },
                    enabled = text.isNotBlank()
                )
                GhostButton(
                    text = "SKIP",
                    onClick = onSkip,
                    color = White30
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
// APP CONTEXT MENU (bottom sheet style)
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun AppContextMenu(
    app: AppInfo,
    isPinned: Boolean,
    hasMindfulDelay: Boolean,
    onDismiss: () -> Unit,
    onTogglePin: () -> Unit,
    onToggleMindful: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Surface1)
                .padding(bottom = 32.dp)
                .clickable(
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    indication = null,
                    onClick = {}  // Consume clicks so background dismiss doesn't trigger
                )
        ) {
            // Handle
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 20.dp)
                    .width(36.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(White10)
                    .align(Alignment.CenterHorizontally)
            )

            // App name
            Text(
                text = app.appName,
                style = MaterialTheme.typography.headlineMedium,
                color = White90,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            Spacer(Modifier.height(8.dp))

            // Options
            ContextMenuOption(
                label = if (isPinned) "Unpin from home" else "Pin to home",
                onClick = onTogglePin
            )
            ContextMenuOption(
                label = if (hasMindfulDelay) "Remove mindful delay" else "Add mindful delay",
                sublabel = if (!hasMindfulDelay) "Pause before opening" else null,
                onClick = onToggleMindful
            )
            ContextMenuOption(
                label = "Cancel",
                color = White30,
                onClick = onDismiss
            )
        }
    }
}

@Composable
private fun ContextMenuOption(
    label: String,
    sublabel: String? = null,
    color: Color = White90,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = color)
        if (sublabel != null) {
            Text(sublabel, style = MaterialTheme.typography.bodyMedium, color = White30)
        }
    }
}

// Needed for DailyIntentionOverlay — basic text field without Material dependency
@Composable
private fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: androidx.compose.ui.text.TextStyle,
    cursorBrush: androidx.compose.ui.graphics.Brush,
    singleLine: Boolean,
    maxLines: Int,
    decorationBox: @Composable (@Composable () -> Unit) -> Unit,
    modifier: Modifier
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        cursorBrush = cursorBrush,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = decorationBox,
        modifier = modifier
    )
}
