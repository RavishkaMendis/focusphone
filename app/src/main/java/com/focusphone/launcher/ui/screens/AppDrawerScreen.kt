package com.focusphone.launcher.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.focusphone.launcher.data.AppInfo
import com.focusphone.launcher.ui.components.*
import com.focusphone.launcher.ui.theme.*
import com.focusphone.launcher.viewmodel.*

/**
 * AppDrawerScreen
 *
 * A clean, searchable list of every app on the device.
 * Alphabetically sorted, text only, no icons.
 *
 * Swipe down or tap the handle at the top to dismiss back to home.
 * Type anything to immediately filter — no "press search" required.
 */
@Composable
fun AppDrawerScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val apps            by viewModel.filteredApps.collectAsState()
    val searchQuery     by viewModel.searchQuery.collectAsState()
    val isLoading       by viewModel.isLoadingApps.collectAsState()
    val pinnedPackages  by viewModel.pinnedPackages.collectAsState()
    val mindfulApps     by viewModel.mindfulPackages.collectAsState()
    val focusActive     by viewModel.focusModeActive.collectAsState()
    val focusAllowed    by viewModel.focusAllowedApps.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var showAppContextMenu by remember { mutableStateOf<AppInfo?>(null) }

    // Group apps by first letter for section headers
    val groupedApps = remember(apps) {
        apps.groupBy { app ->
            val first = app.appName.firstOrNull()?.uppercaseChar() ?: '#'
            if (first.isLetter()) first.toString() else "#"
        }.toSortedMap()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {

            // ── TOP HANDLE / DISMISS ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            keyboardController?.hide()
                            viewModel.clearSearch()
                            onBack()
                        }
                    )
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(3.dp)
                        .background(White30)
                )
            }

            // ── SEARCH BAR ────────────────────────────────────────────────────
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onClear = viewModel::clearSearch,
                focusRequester = focusRequester,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(8.dp))

            // ── APP LIST ──────────────────────────────────────────────────────
            when {
                isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "loading apps...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = White30
                        )
                    }
                }

                apps.isEmpty() && searchQuery.isNotBlank() -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(40.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Column {
                            Text(
                                text = "No app called",
                                style = MaterialTheme.typography.bodyLarge,
                                color = White30
                            )
                            Text(
                                text = "\"$searchQuery\"",
                                style = MaterialTheme.typography.headlineLarge,
                                color = White60
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        if (searchQuery.isBlank()) {
                            // Grouped by letter
                            groupedApps.forEach { (letter, appsInGroup) ->
                                item(key = "header_$letter") {
                                    SectionHeader(letter = letter)
                                }
                                items(
                                    items = appsInGroup,
                                    key = { it.packageName }
                                ) { app ->
                                    AppItem(
                                        name = app.appName,
                                        isPinned = app.packageName in pinnedPackages,
                                        hasMindfulDelay = app.packageName in mindfulApps,
                                        isFocusBlocked = focusActive && app.packageName !in focusAllowed,
                                        onTap = {
                                            keyboardController?.hide()
                                            viewModel.onAppTap(app)
                                        },
                                        onLongPress = { showAppContextMenu = app }
                                    )
                                }
                            }
                        } else {
                            // Flat search results
                            items(
                                items = apps,
                                key = { it.packageName }
                            ) { app ->
                                AppItem(
                                    name = app.appName,
                                    isPinned = app.packageName in pinnedPackages,
                                    hasMindfulDelay = app.packageName in mindfulApps,
                                    isFocusBlocked = focusActive && app.packageName !in focusAllowed,
                                    onTap = {
                                        keyboardController?.hide()
                                        viewModel.onAppTap(app)
                                    },
                                    onLongPress = { showAppContextMenu = app }
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── App Context Menu ──────────────────────────────────────────────────
        showAppContextMenu?.let { app ->
            AppContextMenu(
                app = app,
                isPinned = app.packageName in pinnedPackages,
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

@Composable
private fun SectionHeader(letter: String) {
    Text(
        text = letter,
        style = MaterialTheme.typography.labelSmall,
        color = White30,
        letterSpacing = 2.sp,
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 8.dp)
            .padding(top = 8.dp)
    )
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = MaterialTheme.typography.titleMedium.copy(color = White),
            cursorBrush = SolidColor(White),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (query.isEmpty()) {
                            Text(
                                text = "search apps",
                                style = MaterialTheme.typography.titleMedium,
                                color = White30
                            )
                        }
                        innerTextField()
                    }
                    if (query.isNotEmpty()) {
                        Text(
                            text = "✕",
                            style = MaterialTheme.typography.bodyLarge,
                            color = White30,
                            modifier = Modifier
                                .clickable(onClick = onClear)
                                .padding(8.dp)
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(vertical = 12.dp)
        )
    }

    // Bottom underline
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(if (query.isNotBlank()) White30 else White10)
    )
}
