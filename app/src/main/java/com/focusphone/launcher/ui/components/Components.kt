package com.focusphone.launcher.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.focusphone.launcher.ui.theme.*

// ═══════════════════════════════════════════════════════════════════════
// APP ITEM — Text-only, no icon. The core UI element.
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun AppItem(
    name: String,
    isPinned: Boolean = false,
    hasMindfulDelay: Boolean = false,
    isFocusBlocked: Boolean = false,
    onTap: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }

    val textAlpha by animateFloatAsState(
        targetValue = if (isFocusBlocked) 0.25f else 1f,
        animationSpec = tween(200),
        label = "textAlpha"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,  // No ripple — calm, intentional
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onTap()
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongPress()
                }
            )
            .padding(horizontal = 32.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = textAlpha),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        // Indicator dots — minimal, elegant
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasMindfulDelay) {
                IndicatorDot(color = WarningAmber, contentDescription = "Mindful delay")
            }
            if (isFocusBlocked) {
                IndicatorDot(color = White30, contentDescription = "Blocked in focus")
            }
        }
    }
}

@Composable
private fun IndicatorDot(
    color: Color,
    contentDescription: String,
    size: Dp = 5.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(50))
            .background(color)
    )
}

// ═══════════════════════════════════════════════════════════════════════
// PRIVACY BADGE — Subtle trust signal on home screen
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun PrivacyBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shield icon drawn manually — no dependencies
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(White30)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "no data collected · ever",
            style = MaterialTheme.typography.labelSmall,
            color = White30,
            letterSpacing = 1.sp
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════
// FOCUS MODE BANNER — Shown at top of home when active
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun FocusModeBanner(
    secondsRemaining: Int,
    onEndFocus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Surface1)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(FocusBlue)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "FOCUS",
                style = MaterialTheme.typography.labelSmall,
                color = FocusBlue,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = "%02d:%02d".format(minutes, seconds),
                style = MaterialTheme.typography.titleMedium,
                color = White90
            )
        }

        Text(
            text = "end",
            style = MaterialTheme.typography.labelMedium,
            color = White30,
            modifier = Modifier
                .clickable(onClick = onEndFocus)
                .padding(8.dp)
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════
// SECTION DIVIDER — Ultra-minimal, 1px line
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun SectionDivider(modifier: Modifier = Modifier) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(1.dp)
            .background(White10)
    )
}

// ═══════════════════════════════════════════════════════════════════════
// SETTINGS TOGGLE ROW
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun SettingsToggleRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = White90
            )
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = White30
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Black,
                checkedTrackColor = White90,
                uncheckedThumbColor = White30,
                uncheckedTrackColor = Surface2
            )
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════
// SETTINGS NAVIGATION ROW
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun SettingsNavRow(
    title: String,
    subtitle: String? = null,
    value: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = White90
            )
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = White30
                )
            }
        }
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = White30
            )
        } else {
            Text(
                text = "›",
                style = MaterialTheme.typography.headlineMedium,
                color = White30
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════
// GHOST BUTTON — Minimal outlined action button
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = White
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = color,
            letterSpacing = 1.sp
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════
// SOLID BUTTON — Primary action
// ═══════════════════════════════════════════════════════════════════════

@Composable
fun SolidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (enabled) White else White30)
            .clickable(enabled = enabled) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
            .padding(horizontal = 32.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Black,
            letterSpacing = 1.sp
        )
    }
}
