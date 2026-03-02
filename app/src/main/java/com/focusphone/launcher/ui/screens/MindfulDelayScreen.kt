package com.focusphone.launcher.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.material3.*
import com.focusphone.launcher.ui.theme.*
import kotlinx.coroutines.delay

/**
 * MindfulDelayScreen
 *
 * The most important feature for breaking unconscious phone habits.
 *
 * When you tap an app with mindful delay enabled:
 *   1. This screen appears instantly (full-screen, black)
 *   2. A calm circular countdown plays (default: 5 seconds)
 *   3. A simple question: "Do you really need this right now?"
 *   4. Two options: wait and proceed, or go back intentionally
 *
 * Psychology: The 5 seconds aren't meant to block. They're meant to
 * create a moment of conscious choice. That's all it takes to break
 * the reflexive tap-scroll cycle.
 */
class MindfulDelayActivity : ComponentActivity() {

    companion object {
        const val EXTRA_PACKAGE_NAME = "package_name"
        const val EXTRA_APP_NAME     = "app_name"
        const val EXTRA_DELAY_SECS   = "delay_seconds"

        fun createIntent(
            context: Context,
            packageName: String,
            appName: String,
            delaySeconds: Int = 5
        ): Intent = Intent(context, MindfulDelayActivity::class.java).apply {
            putExtra(EXTRA_PACKAGE_NAME, packageName)
            putExtra(EXTRA_APP_NAME, appName)
            putExtra(EXTRA_DELAY_SECS, delaySeconds)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val packageName  = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: run { finish(); return }
        val appName      = intent.getStringExtra(EXTRA_APP_NAME) ?: packageName
        val delaySecs    = intent.getIntExtra(EXTRA_DELAY_SECS, 5)

        setContent {
            FocusPhoneTheme {
                MindfulDelayScreen(
                    appName = appName,
                    totalSeconds = delaySecs,
                    onProceed = {
                        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                        launchIntent?.let {
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(it)
                        }
                        finish()
                    },
                    onCancel = { finish() }
                )
            }
        }
    }
}

@Composable
fun MindfulDelayScreen(
    appName: String,
    totalSeconds: Int,
    onProceed: () -> Unit,
    onCancel: () -> Unit
) {
    var secondsLeft by remember { mutableStateOf(totalSeconds) }
    var proceeded   by remember { mutableStateOf(false) }

    // Countdown
    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1_000)
            secondsLeft--
        }
        // Auto-proceed when countdown ends
        if (!proceeded) {
            proceeded = true
            onProceed()
        }
    }

    // Arc progress animation (0f = full circle, 1f = complete)
    val progress = 1f - (secondsLeft.toFloat() / totalSeconds.toFloat())
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "countdown"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null,
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            Spacer(Modifier.weight(1f))

            // App name
            Text(
                text = appName,
                style = MaterialTheme.typography.headlineLarge,
                color = White60,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(64.dp))

            // Countdown ring
            CountdownRing(
                progress = animatedProgress,
                secondsLeft = secondsLeft,
                totalSeconds = totalSeconds
            )

            Spacer(Modifier.height(48.dp))

            // Question
            Text(
                text = "Is this intentional?",
                style = MaterialTheme.typography.bodyLarge,
                color = White30,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GhostButton(
                    text = "GO BACK",
                    onClick = onCancel,
                    color = White30,
                    modifier = Modifier.weight(1f)
                )
                SolidButton(
                    text = "OPEN",
                    onClick = {
                        proceeded = true
                        onProceed()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(60.dp))
        }
    }
}

@Composable
private fun CountdownRing(
    progress: Float,
    secondsLeft: Int,
    totalSeconds: Int
) {
    val ringColor = WarningAmber.copy(alpha = 0.8f)
    val trackColor = White10

    Box(
        modifier = Modifier
            .size(120.dp)
            .drawWithContent {
                // Track
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
                // Progress
                drawArc(
                    color = ringColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
                drawContent()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = secondsLeft.toString(),
            style = MaterialTheme.typography.displayMedium,
            color = if (secondsLeft <= 2) WarningAmber else White60
        )
    }
}
