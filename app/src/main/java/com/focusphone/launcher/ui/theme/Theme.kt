package com.focusphone.launcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * FocusPhone Theme
 *
 * Strictly dark. There is no light mode.
 * A phone built for focus should feel the same at 3am as at 3pm.
 * Consistent, calm, unhurried.
 */

private val FocusPhoneColorScheme = darkColorScheme(
    primary            = White,
    onPrimary          = Black,
    primaryContainer   = Surface2,
    onPrimaryContainer = White90,

    secondary          = White60,
    onSecondary        = Black,
    secondaryContainer = Surface1,
    onSecondaryContainer = White60,

    tertiary           = White30,
    onTertiary         = Black,

    background         = Black,
    onBackground       = White,

    surface            = Surface1,
    onSurface          = White,
    surfaceVariant     = Surface2,
    onSurfaceVariant   = White60,

    outline            = White10,
    outlineVariant     = White10,

    error              = ErrorRed,
    onError            = Black,

    scrim              = Color(0xCC000000),
)

@Composable
fun FocusPhoneTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FocusPhoneColorScheme,
        typography = FocusPhoneTypography,
        content = content
    )
}
