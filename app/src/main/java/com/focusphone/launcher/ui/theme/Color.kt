package com.focusphone.launcher.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * FocusPhone Design System — Color Palette
 *
 * Philosophy: Monochromatic. Every color choice reduces visual stimulus.
 * The phone should feel like a high-quality notebook, not a slot machine.
 */

// ── Core Backgrounds ─────────────────────────────────────────────────────────
val Black           = Color(0xFF000000)   // True black — OLED-optimized, saves battery
val Surface1        = Color(0xFF0D0D0D)   // Cards, bottom sheets
val Surface2        = Color(0xFF161616)   // Elevated surfaces
val Surface3        = Color(0xFF1F1F1F)   // Highest elevation

// ── Text ─────────────────────────────────────────────────────────────────────
val White           = Color(0xFFFFFFFF)   // Primary text
val White90         = Color(0xE6FFFFFF)   // Secondary text (90% opacity)
val White60         = Color(0x99FFFFFF)   // Tertiary text — labels, captions
val White30         = Color(0x4DFFFFFF)   // Disabled / placeholder
val White10         = Color(0x1AFFFFFF)   // Dividers, subtle borders

// ── Accent — Single, muted. Used sparingly. ──────────────────────────────────
val Accent          = Color(0xFFE8E8E8)   // Near-white — highlights only
val AccentSubtle    = Color(0x26E8E8E8)   // Ghost accent for backgrounds

// ── Semantic Colors ───────────────────────────────────────────────────────────
val FocusBlue       = Color(0xFF4A9EFF)   // Focus mode active indicator
val SuccessGreen    = Color(0xFF4ADE80)   // Positive feedback
val WarningAmber    = Color(0xFFFBBF24)   // Mindful delay countdown
val ErrorRed        = Color(0xFFFF4A4A)   // Errors only

// ── Gradient stops ────────────────────────────────────────────────────────────
val GradientTop     = Color(0xFF000000)
val GradientBottom  = Color(0xFF050505)
