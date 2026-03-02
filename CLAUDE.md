# CLAUDE.md — FocusPhone
### Everything Claude needs to know about this project

---

## What This Is

FocusPhone is a **privacy-first, minimalist Android launcher** built for young entrepreneurs, side hustlers, and high-performance individuals who want to use their phone as a tool — not get used by it.

This is a **real commercial product** being built for the Google Play Store. Every decision — code, design, copy, features — should reflect that. We are building something that competes at the highest level. Think Apple under Steve Jobs: obsessive quality, nothing unnecessary, every pixel deliberate.

---

## The Origin of This Project

### The Competitor We're Beating

The reference app is **Minimalist Phone** by QQ Labs (`com.qqlabs.minimalistlauncher`). It has strong traction and proves the market exists. But it has two critical weaknesses we exploit:

1. **It collects and shares personal info, app activity, and more with third parties** — visible right on its Play Store data safety section. For an app claiming to help people focus and detox, this is a hypocritical betrayal of trust.
2. **It runs on a subscription model** — the privacy and minimalism community despises ongoing fees. They see it as the same extractive behavior they're trying to escape from.

Our answer to both: **zero data collected, ever** and **one-time payment**.

### The Vision Statement

> Not a detox app. Not a recovery tool.
> A phone for people who are building something.
> Your phone — stripped of everything designed to waste your time,
> and none of your data collected. Ever.

---

## Target Audience

### Primary — The Focused Hustler

- **Age:** 18–32
- **Identity:** Young entrepreneur, side hustler, freelancer, content creator, student building something
- **Mindset:** Grinding toward financial freedom, a business, or a creative output. Time is literally money to them.
- **Influences:** Alex Hormozi, Andrew Huberman, Cal Newport, Hamza, David Goggins energy
- **Core problem:** Losing hours per day to social media instead of building their business or skills
- **Phone behavior:** Checks phone 300+ times/day. Opens social apps reflexively, not intentionally. Picks up phone for one thing, loses 20 minutes.
- **What they spend money on:** Courses, tools, subscriptions that help them grow. Won't hesitate at $2.99 for something that genuinely helps.
- **Core desire:** To feel in control. Disciplined. Intentional with their time.
- **Deepest fear:** Wasting their prime years scrolling while others are building.

### Secondary — The Mindful Professional

Ages 28–40. Works in tech, creative, or professional services. Already familiar with digital wellness. Has tried multiple apps and been let down. Values privacy strongly. Disposable income.

### Secondary — ADHD & Neurodivergent Users

A large, loyal, vocal segment. "Out of sight, out of mind" works neurologically for them. This community shares tools heavily — huge organic growth potential.

---

## Market Research Findings

These are the proven truths from user research, forums, reviews, and behavioral psychology. Reference these when making product decisions.

### Why People Switch to Minimalist Launchers (in order of importance)

1. **Exhaustion from distraction** — Not philosophy. A breaking point. They pick up their phone "for one thing" and lose 15–30 minutes to nothing. Every time.
2. **Muscle memory betrayal** — The brain builds habits. Users open Instagram, TikTok, Reddit without consciously deciding to. The default launcher UI is engineered around dopamine loops.
3. **Mental health & anxiety** — Heavy phone use is directly connected to anxiety, comparison, stress, and broken sleep. People want a phone that feels *calm*.
4. **Productivity & deep work** — Driven by Cal Newport's "Digital Minimalism" and "Deep Work." Research shows it takes 23 minutes to regain focus after a single interruption. These users are protecting their attention like an asset.
5. **Privacy concerns** — 2024 saw multiple Play Store apps install malware. Trust in apps is at a low. Users are waking up to being the product.

### The Key Statistic

The average person checks their phone **344 times per day** and spends **7 hours on screens**. Digital minimalists report reclaiming **2–3 hours of deep work per day**.

### What Actually Works (Psychologically)

- **Out of sight, out of mind** — Removing colorful icons is the single most effective mechanism. No visual trigger = no reflex tap.
- **Passive friction over active blocking** — Hard blocks get disabled when tempted. A 5-second mindful delay pause works better because it creates a *moment of choice*, not a wall.
- **Breaking muscle memory** — Text-only app lists force conscious thought. You have to *decide* to open an app instead of muscle-memory tapping.
- **Identity positioning** — Users who see themselves as "high performers" respond to tools that reinforce that identity. This is not a "detox app for addicts." It's a weapon for builders.

### Competitor Pain Points (What Users Hate About Existing Apps)

- Subscription pricing ("predatory for a launcher")
- Data collection by a "minimalist" app (direct contradiction)
- Feature creep — starts clean, gradually adds bloat
- Ads inside the app
- Active blockers they just bypass when they want to scroll
- Bugs and freezing
- No control over what's shown (too rigid)
- Too extreme (can't use Maps, banking, WhatsApp for work)

---

## Privacy Philosophy

This is the bedrock of the entire product. It is non-negotiable.

### The Technical Guarantee

**FocusPhone requests zero internet permissions.** This is not a policy — it is architectural. Android cannot let the app reach a server. It is physically impossible for data to leave the device.

- No `INTERNET` permission in `AndroidManifest.xml` — ever
- No Firebase (not even crash reporting)
- No analytics SDK of any kind
- No third-party libraries that phone home
- No accounts, no login, no sign-in with Google
- No cloud sync of any kind
- All settings stored locally via Android DataStore only
- The Play Store data safety section will show: **"No data collected"** — a visible, rare, trust-building signal

### How to Talk About Privacy in Code Comments

Privacy comments in the codebase should be clear and human. Example from `FocusPhoneApp.kt`:
```kotlin
// Intentionally empty — no third-party SDKs, no telemetry, no phone-home calls.
// Your data stays on your device.
```

---

## Design Philosophy

### The Core Principle

> The phone should feel like a high-quality notebook, not a slot machine.

Every design decision reduces stimulus. Every element must earn its place.

### Rules That Cannot Be Broken

1. **No icons on the home screen.** App names only. Icons are visual triggers designed by social media companies to grab attention. Text forces intentional, conscious action.
2. **True black background (#000000).** OLED-optimized. Saves battery. Feels premium. No dark grays, no gradients.
3. **No notification badges.** Red dots are engineered dopamine triggers. They don't belong here.
4. **No infinite scroll anywhere.**
5. **One accent color maximum.** Currently: near-white. Nothing colorful unless semantically necessary (Focus Blue for active sessions, Warning Amber for mindful delay countdown).
6. **Haptic feedback on every tap.** Physical confirmation without sound. Intentional, satisfying.
7. **No ripple effects.** Calm interactions only. The phone should feel unhurried.
8. **Typography hierarchy through weight, not size.** Ultra-light (W100) for the clock — meditative, not aggressive. W300 for app names — readable but not demanding attention.

### Color Tokens (use these, never hardcode hex values)

```kotlin
Black         = #000000   // backgrounds only
White         = #FFFFFF   // primary text
White90       = #E6FFFFFF // secondary text
White60       = #99FFFFFF // tertiary text
White30       = #4DFFFFFF // disabled, labels
White10       = #1AFFFFFF // dividers
FocusBlue     = #4A9EFF   // focus mode active — use sparingly
WarningAmber  = #FBBF24   // mindful delay countdown only
SuccessGreen  = #4ADE80   // positive confirmations only
Surface1      = #0D0D0D   // cards
Surface2      = #161616   // elevated surfaces
```

### Typography Rules

- Clock: `displayLarge` — 72sp, W100
- App names in list: `titleMedium` — 17sp, W300
- Section labels: `labelSmall` — 10sp, W400, 2sp letter spacing, ALL CAPS
- Body copy: `bodyLarge` — 16sp, W400
- Nothing bold unless it truly earns it (e.g. a screen title)

---

## Technical Architecture

### Stack

- **Language:** Kotlin only. No Java.
- **UI:** Jetpack Compose + Material3
- **Architecture:** MVVM — `MainViewModel` is the single source of truth
- **Persistence:** Android DataStore (local only)
- **Navigation:** State-driven with sealed class `Screen` — no Navigation component needed
- **Target SDK:** 35 (Android 15)
- **Min SDK:** 26 (Android 8.0) — broad coverage without ancient API constraints
- **Build system:** Gradle KTS only

### Dependency Rules

**Allowed:**
- Jetpack Compose BOM
- Material3
- AndroidX Lifecycle / ViewModel
- DataStore Preferences
- Kotlin Coroutines
- Navigation Compose (if needed)

**Banned — forever:**
- Firebase (any module)
- Google Analytics
- Any crash reporter that phones home (Crashlytics, Sentry, etc.)
- Any ad SDK
- Any third-party analytics library
- Retrofit / OkHttp (there's no server to talk to)
- Room (DataStore is sufficient for our use case)

### File Structure

```
app/src/main/java/com/focusphone/launcher/
├── FocusPhoneApp.kt              ← Application class (intentionally minimal)
├── MainActivity.kt               ← Single activity, state-driven nav
├── data/
│   ├── AppInfo.kt                ← Core data models + Screen sealed class
│   ├── AppRepository.kt          ← Reads installed apps from PackageManager
│   └── PreferencesManager.kt     ← All local persistence
├── viewmodel/
│   └── MainViewModel.kt          ← Single source of truth for all state
└── ui/
    ├── theme/
    │   ├── Color.kt              ← All color tokens
    │   ├── Type.kt               ← Typography scale
    │   └── Theme.kt              ← Material3 dark theme config
    ├── components/
    │   └── Components.kt         ← All reusable UI primitives
    └── screens/
        ├── HomeScreen.kt         ← Clock, intention, pinned apps
        ├── AppDrawerScreen.kt    ← Searchable full app list
        ├── MindfulDelayScreen.kt ← Countdown overlay + Activity
        ├── SettingsScreen.kt     ← All settings sub-screens
        └── OnboardingScreen.kt   ← First-time 4-screen flow
```

### Key Architectural Decisions

**Single Activity.** Everything runs in `MainActivity`. No fragment transactions, no complex back stacks. Navigation is a `var currentScreen by remember { mutableStateOf<Screen>(...) }`. Simple is correct here.

**ViewModel is the brain.** Screens are dumb. They observe state and call ViewModel methods. Zero business logic in composables.

**DataStore over SharedPreferences.** Coroutine-safe, type-safe, no corruption on process death.

**`launchSingleTask` on MainActivity.** Ensures pressing home always returns to the same instance. Critical for launcher behavior.

**`MindfulDelayActivity` is separate.** The mindful delay must launch as a separate Activity so it can intercept app launches correctly at the OS level.

---

## Features (MVP)

### Shipped in MVP

| Feature | Description |
|---|---|
| Text-only home screen | App names only — no icons, no color triggers |
| Live clock | Ultra-light typography, updates every second |
| Date display | Below clock, tertiary color |
| Pinned apps | Up to 8 apps on home screen, user-selected |
| Daily intention | Morning prompt: "What's your #1 focus today?" |
| App drawer | Full alphabetical list with instant search + section headers |
| Long-press context menu | Pin/unpin, add/remove mindful delay |
| Mindful delay | Countdown overlay (3–30s, adjustable) before opening selected apps |
| Focus mode | Whitelist-based app blocking with countdown timer |
| Settings | Pin apps, mindful delay config, focus mode setup, privacy info |
| Onboarding | 4-screen first-time flow |
| Privacy badge | Subtle "no data collected · ever" on home screen |
| OLED-optimized | True black (#000000) throughout |

### Planned Post-MVP (Roadmap)

1. **Grayscale mode** — system-level monochrome toggle shortcut
2. **Deep Work timer** — Pomodoro accessible from home screen
3. **App usage stats** — shown locally only, never transmitted
4. **Scheduled focus** — automatic focus mode at set times
5. **Clock widget** — for lock screen
6. **Pro unlock** — one-time purchase via Google Play Billing ($2.99)
7. **Supporter pack** — optional $9.99 for people who want to back the project

### Features That Will NEVER Be Added

- Accounts of any kind
- Cloud sync of any kind
- Social sharing
- Gamification or streaks (contradicts the calm philosophy)
- Ads
- Subscription pricing
- Notification history viewer (privacy concern)
- Anything that requires internet permission

---

## Monetization Plan

| Tier | Price | What's Included |
|---|---|---|
| Free | $0 | Core launcher, text home screen, pinned apps, dark mode |
| Pro | $2.99 one-time | Mindful delay, Focus Mode, Daily Intentions, Deep Work timer, advanced customization |
| Supporter Pack | $9.99 optional | Same as Pro + bragging rights for backing open-source privacy software |
| NEVER | — | Subscriptions, ads, data monetization |

---

## Brand & Marketing

### App Name

**FocusPhone** (current working title). Alternatives considered:
- Grind OS
- Basecamp Launcher
- Zerotrail
- Focus Shell
- Vault Phone

### Key Marketing Messages

- *"Your phone should work for you. Not collect data on you."*
- *"Built for builders. Not scrollers."*
- *"The only launcher that asks for zero internet permissions."*
- *"No ads. No tracking. No subscriptions. Just focus."*

### Play Store Trust Signal

The data safety section will show **"No data collected"** — this is rare and immediately visible. It is our most powerful marketing asset.

### Target Communities

- Reddit: `r/digitalminimalism`, `r/Entrepreneur`, `r/productivity`, `r/nosurf`
- ProductHunt — launch day
- IndieHackers — indie founder community
- TikTok / YouTube Shorts — "I changed my phone setup and built my business faster" content
- Twitter/X — productivity and entrepreneurship circles

---

## Coding Guidelines

When writing or modifying code for this project, follow these rules without exception.

### General

- **Kotlin only.** No Java files, ever.
- **Compose only.** No XML layouts (except themes.xml and AndroidManifest.xml which are required).
- **Every function should have a clear single responsibility.**
- **Comment the "why", not the "what".** Code explains what. Comments explain intent, tradeoffs, and non-obvious decisions.
- **No unused imports, no dead code.** Keep it clean.

### Privacy

- **Never add the INTERNET permission.** If a feature requires it, redesign the feature.
- **Never add Firebase or any analytics library.** If something goes wrong, debug with local logs.
- **Never store anything outside DataStore.** No SharedPreferences, no files, no SQLite.
- **Never add any dependency that phones home.** Audit every new library before adding it.

### Compose UI

- **Dark theme only.** There is no light mode. Don't build one.
- **Use design tokens from `Color.kt`.** Never hardcode hex values in composables.
- **No ripple effects.** Use `indication = null` on clickables for a calm feel.
- **Haptic feedback on every user tap.** `HapticFeedbackType.LongPress` on both taps and long presses.
- **Animations should be subtle and fast.** `tween(200–400ms)`. Never flashy.
- **Avoid `Modifier.fillMaxWidth()` padding inconsistency.** Use `padding(horizontal = 24.dp)` or `32.dp` consistently throughout.
- **No icons from Material Icons.** We use text and simple Box shapes instead. Keeps the app icon-free and lightweight.

### State Management

- **All state lives in `MainViewModel`.** Screens do not hold business logic.
- **Use `StateFlow` for all observable state.** `collectAsState()` in composables.
- **Use `combine()` for derived state.** Don't duplicate logic.
- **`viewModelScope.launch` for all suspend calls from ViewModel.**

### Naming Conventions

- Files: `PascalCase.kt`
- Composables: `PascalCase` with descriptive nouns (`HomeScreen`, `AppItem`, `FocusModeBanner`)
- ViewModels: suffix `ViewModel`
- State flows: descriptive, no Hungarian notation (`pinnedApps`, not `mPinnedApps`)
- Constants in companion objects: `SCREAMING_SNAKE_CASE`
- Private composables within a file: prefix with `private`

### Performance

- **Lazy lists for any list over 5 items.** Always `LazyColumn`, never `Column` with a loop for app lists.
- **`key` parameter on all `items()` calls.** Use `packageName` as the key for app lists.
- **`remember` for expensive computations.** Especially `groupBy` in AppDrawerScreen.
- **`stateIn` with `SharingStarted.WhileSubscribed(5_000)`.** Standard pattern — don't deviate.

---

## Quality Standard

The baseline for every decision is: **would this ship in an Apple product under Steve Jobs?**

That means:
- No half-finished features. Ship it right or don't ship it.
- No placeholder UI. Every screen looks intentional.
- No inconsistent spacing. Eyeball everything.
- No feature that doesn't serve a clear user need backed by research.
- No "we'll clean it up later." Clean it up now.
- If something feels slightly off, it is off. Fix it.

This is not a hobby project. It's a commercial product with real users who will pay real money and trust it with their daily phone experience. Treat it accordingly.

---

## Quick Reference

| Thing | Value |
|---|---|
| Package name | `com.focusphone.launcher` |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 (Android 15) |
| Language | Kotlin |
| UI | Jetpack Compose + Material3 |
| Persistence | DataStore only |
| Internet permission | NEVER |
| Third-party SDKs | NONE |
| Theme | Dark only, true black |
| Primary font | System default (best OS rendering) |
| Monetization | One-time Pro purchase, $2.99 |
| Competitor | com.qqlabs.minimalistlauncher |
