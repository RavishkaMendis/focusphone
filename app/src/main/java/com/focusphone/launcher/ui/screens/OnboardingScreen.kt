package com.focusphone.launcher.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.focusphone.launcher.ui.components.*
import com.focusphone.launcher.ui.theme.*

/**
 * OnboardingScreen
 *
 * Shown once, on first launch.
 * 4 cards. Simple. Direct. Sets the right expectations.
 *
 * Card 1: Welcome — what this is
 * Card 2: Privacy — our promise
 * Card 3: How it works — 3 gestures to know
 * Card 4: Let's go — set their first intention
 */
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = 4

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(200))
            },
            label = "onboarding"
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    overline   = "WELCOME",
                    headline   = "A phone built for building.",
                    body       = "Not for scrolling. Not for distraction. For people who have things to create, build, and ship.",
                    cta        = "NEXT",
                    onCta      = { currentPage++ }
                )
                1 -> OnboardingPage(
                    overline   = "PRIVACY",
                    headline   = "Zero data. Zero exceptions.",
                    body       = "This app has no internet permission. Nothing about how you use your phone ever leaves your device. No accounts, no analytics, no ads. Ever.",
                    cta        = "NEXT",
                    onCta      = { currentPage++ },
                    badge      = "• no internet permission requested"
                )
                2 -> OnboardingPage(
                    overline   = "HOW IT WORKS",
                    headline   = "Three things to know.",
                    body       = "Long press home screen → Settings\n\nSwipe up or tap ↑ → All Apps\n\nTap once → Open app",
                    cta        = "NEXT",
                    onCta      = { currentPage++ },
                    isMonospace = true
                )
                3 -> OnboardingPage(
                    overline   = "READY",
                    headline   = "Start with intention.",
                    body       = "Every morning, we'll ask you one question: what's your #1 focus today? That's it. No more, no less.",
                    cta        = "LET'S GO",
                    onCta      = onComplete
                )
            }
        }

        // Progress dots
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(totalPages) { i ->
                Box(
                    modifier = Modifier
                        .size(if (i == currentPage) 20.dp else 6.dp, 6.dp)
                        .background(
                            color = if (i == currentPage) White else White30,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(3.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    overline: String,
    headline: String,
    body: String,
    cta: String,
    onCta: () -> Unit,
    badge: String? = null,
    isMonospace: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(40.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Spacer(Modifier.height(40.dp))

            Text(
                text = overline,
                style = MaterialTheme.typography.labelSmall,
                color = White30,
                letterSpacing = 3.sp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = headline,
                style = MaterialTheme.typography.displayMedium,
                color = White
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = body,
                style = if (isMonospace)
                    MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        lineHeight = 32.sp
                    )
                else
                    MaterialTheme.typography.bodyLarge,
                color = White60,
                lineHeight = 26.sp
            )

            if (badge != null) {
                Spacer(Modifier.height(32.dp))
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelMedium,
                    color = SuccessGreen,
                    letterSpacing = 1.sp
                )
            }
        }

        SolidButton(
            text = cta,
            onClick = onCta
        )
    }
}
