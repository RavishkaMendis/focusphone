# FocusPhone — Privacy-First Minimalist Launcher

<div align="center">

**Your phone should work for you. Not collect data on you.**

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Open Source](https://img.shields.io/badge/Open%20Source-%E2%9D%A4-red)](CONTRIBUTING.md)
[![Privacy First](https://img.shields.io/badge/Privacy-First-green)](SECURITY.md)
[![No Tracking](https://img.shields.io/badge/Tracking-None-brightgreen)](SECURITY.md)

*Built for builders. Not scrollers.*

[Features](#what-youve-built) • [Build Guide](#build-instructions) • [Contributing](CONTRIBUTING.md) • [Security](SECURITY.md)

</div>

---

## 🔒 Why Open Source?

For a privacy-focused app, **open source isn't optional — it's essential**.

You shouldn't have to trust us. You should be able to **verify**:
- ✅ Zero internet permission (search `AndroidManifest.xml` yourself)
- ✅ No analytics SDKs (audit `build.gradle.kts`)
- ✅ No tracking code (grep the entire source)
- ✅ Local storage only (see `PreferencesManager.kt`)

**Trust through transparency.** Not through promises.

→ Read our [Security Policy](SECURITY.md) for full audit instructions

---

## 🚀 Quick Start: Build APK for Testing

**Want to test on your Galaxy Flip now?** → See [QUICK_START.md](QUICK_START.md)

**Need detailed instructions?** → See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)

**TL;DR:**
1. Install [Android Studio](https://developer.android.com/studio)
2. Open this project folder
3. Build → Build APK(s)
4. Transfer `app-debug.apk` to your phone and install

---

## What You've Built

A complete, production-ready Android launcher app. Every line of code written from scratch. No templates, no boilerplate generators.

**Features in this MVP:**
- ✅ Text-only home screen (no icons — breaks unconscious tapping)
- ✅ Live clock with ultra-light typography
- ✅ Daily intention prompt (every morning, one question)
- ✅ Swipe-up app drawer with instant search + alphabetical grouping
- ✅ Long-press to pin/unpin apps from home
- ✅ Mindful delay — customizable countdown before opening distracting apps
- ✅ Focus Mode — whitelist-based app blocking with timer
- ✅ Long-press home → Settings
- ✅ Clean onboarding (4 screens)
- ✅ Privacy badge on home screen
- ✅ Zero internet permission
- ✅ All data stored locally via DataStore
- ✅ OLED-optimized true black theme

---

## Build Instructions

### Option 1: Android Studio (Recommended)

**1. Prerequisites**
- [Android Studio](https://developer.android.com/studio) (includes JDK and Android SDK)
- Android device running Android 8.0+ (API 26+) or emulator

**2. Open the Project**
1. Launch Android Studio
2. File → Open → select this `focusphone/` folder
3. Wait for Gradle sync to complete (5-10 minutes first time)

**3. Build APK for Testing**
1. Build → Build Bundle(s) / APK(s) → Build APK(s)
2. Wait for build to complete (1-3 minutes)
3. Click "locate" in success notification
4. Copy `app-debug.apk` to your phone

**4. Install on Your Phone**
1. Transfer APK to phone's Downloads folder
2. Settings → Security → Install Unknown Apps → Enable for Files
3. Open APK file and tap Install
4. Press Home button → Select FocusPhone → Always

### Option 2: Direct Run from Android Studio

**1. Connect Your Phone**
- Enable Developer Options: Settings → About Phone → Tap "Build Number" 7 times
- Enable USB Debugging: Settings → Developer Options → USB Debugging
- Connect via USB cable

**2. Run**
- Click ▶ Run button in Android Studio
- Select your device from the list
- App will install and launch automatically

**3. Set as Default Launcher**
- Press Home button on phone
- Dialog appears: "Which home app?"
- Select **FocusPhone** → **Always**

---

## File Structure

```
app/src/main/java/com/focusphone/launcher/
├── FocusPhoneApp.kt              ← Application class (intentionally minimal)
├── MainActivity.kt               ← Single activity, state-driven navigation
├── data/
│   ├── AppInfo.kt                ← Data models
│   ├── AppRepository.kt          ← Reads installed apps from PackageManager
│   └── PreferencesManager.kt     ← All local persistence (DataStore)
├── viewmodel/
│   └── MainViewModel.kt          ← All app state + business logic
└── ui/
    ├── theme/
    │   ├── Color.kt              ← Full design token system
    │   ├── Type.kt               ← Typography scale
    │   └── Theme.kt              ← Material3 dark theme
    ├── components/
    │   └── Components.kt         ← Reusable UI primitives
    └── screens/
        ├── HomeScreen.kt         ← Clock, intention, pinned apps
        ├── AppDrawerScreen.kt    ← Searchable full app list
        ├── MindfulDelayScreen.kt ← Countdown overlay + Activity
        ├── SettingsScreen.kt     ← Pin apps, mindful delay, focus mode
        └── OnboardingScreen.kt   ← 4-screen first-time flow
```

---

## Play Store Checklist

### Before Submitting:
- [ ] Change package name: `com.focusphone.launcher` → your own unique ID
- [ ] Create a real app icon (replace the vector placeholder)
- [ ] Set up a Google Play Developer Account ($25 one-time)
- [ ] Take screenshots on a real device (Play Store requires at least 2)
- [ ] Write your store listing (use the research doc for copywriting)

### Play Store Data Safety Section:
When asked about data collection:
- **"Does your app collect or share any of the required user data types?"** → NO
- This will show a green "No data collected" badge on your store listing — a major trust signal

### Pricing:
- Free base app
- $2.99 Pro unlock via Play Billing (add later — not in MVP)

---

## Next Features (Post-MVP Roadmap)

1. **Widget** — minimal clock widget for the lock screen
2. **Grayscale Mode** — system-level grayscale toggle shortcut
3. **Deep Work Timer** — Pomodoro accessible from home screen
4. **App Usage Stats** — shown locally only, never transmitted
5. **Scheduled Focus** — automatic focus mode at set times
6. **Pro unlock** — one-time purchase via Google Play Billing
7. **Theming** — light/sepia/custom tint options

---

## Privacy Audit (for your marketing)

Run these checks before launch to back up your claims:

```bash
# Confirm no internet permission in APK
aapt dump permissions app-release.apk | grep INTERNET
# Should return nothing

# Check for network calls at runtime
adb shell dumpsys connectivity | grep focusphone
# Should show no active connections
```

---

*Built with: Kotlin · Jetpack Compose · Material3 · DataStore · 0 third-party SDKs*
*Privacy: Zero internet permissions · Zero data collection · Zero analytics*
