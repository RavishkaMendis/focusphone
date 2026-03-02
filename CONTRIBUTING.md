# Contributing to FocusPhone

Thank you for your interest in contributing to FocusPhone! This document provides guidelines and instructions for contributing.

---

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Code Guidelines](#code-guidelines)
- [Pull Request Process](#pull-request-process)
- [Privacy & Security Rules](#privacy--security-rules)

---

## Code of Conduct

### Our Standards

- Be respectful and inclusive
- Focus on what's best for the community
- Show empathy towards other contributors
- Accept constructive criticism gracefully
- Focus on the code, not the person

### Unacceptable Behavior

- Harassment, trolling, or derogatory comments
- Personal or political attacks
- Publishing others' private information
- Spam or off-topic discussions

---

## How Can I Contribute?

### Reporting Bugs

**Before submitting a bug report:**
1. Check existing issues to avoid duplicates
2. Test on latest version
3. Gather reproduction steps

**Good bug reports include:**
- Clear, descriptive title
- Steps to reproduce
- Expected vs actual behavior
- Screenshots/videos (if applicable)
- Device info (manufacturer, model, Android version)
- FocusPhone version

**Example:**
```markdown
**Title:** App drawer search crashes on Samsung Galaxy S21

**Steps to reproduce:**
1. Open app drawer (swipe up from home)
2. Type "chr" in search box
3. App crashes

**Expected:** Search filters apps containing "chr"
**Actual:** App crashes to Android home

**Device:** Samsung Galaxy S21, Android 14
**FocusPhone:** v1.0.0 (build 1)
```

### Suggesting Features

We love feature ideas! But FocusPhone has a specific philosophy: **minimalism and privacy first**.

**Good feature requests:**
- Align with minimalist philosophy
- Don't require internet permission
- Don't add third-party dependencies
- Have clear user benefit (not just "nice to have")
- Include mockups or detailed descriptions

**Features we'll likely reject:**
- Anything requiring internet permission
- Social features, sharing, accounts
- Gamification, streaks, points
- Features that add visual clutter
- Anything requiring analytics

### Contributing Code

We welcome code contributions! See [Development Setup](#development-setup) below.

### Improving Documentation

- Fix typos or unclear explanations
- Add missing documentation
- Translate documentation (future)
- Create tutorials or guides

### Spreading the Word

- Star the repo on GitHub
- Share on Reddit (r/digitalminimalism, r/privacy)
- Write blog posts or reviews
- Create YouTube videos
- Help answer questions in Discussions

---

## Development Setup

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17+ (included with Android Studio)
- Android SDK (API 26-35)
- Git

### Setup Steps

```bash
# Clone the repository
git clone https://github.com/[your-username]/focusphone.git
cd focusphone

# Open in Android Studio
# File → Open → Select focusphone folder

# Wait for Gradle sync to complete

# Run on device or emulator
# Click ▶ Run button
```

### Project Structure

```
app/src/main/java/com/focusphone/launcher/
├── FocusPhoneApp.kt              ← Application class
├── MainActivity.kt               ← Single activity
├── data/                         ← Data layer
│   ├── AppInfo.kt                  (Models)
│   ├── AppRepository.kt            (Package Manager wrapper)
│   └── PreferencesManager.kt       (DataStore)
├── viewmodel/                    ← Business logic
│   └── MainViewModel.kt
└── ui/                           ← Presentation layer
    ├── theme/                      (Colors, typography, theme)
    ├── components/                 (Reusable UI)
    └── screens/                    (Full screens)
```

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

---

## Code Guidelines

### Architecture

- **MVVM pattern** - ViewModel holds state, UI observes
- **Single Activity** - No fragments, simple navigation
- **Jetpack Compose** - 100% Compose, zero XML layouts
- **Unidirectional data flow** - UI calls ViewModel methods, ViewModel updates StateFlow

### Kotlin Style

```kotlin
// Use explicit types for public APIs
fun getAppList(): List<AppInfo> { ... }

// Use type inference for local variables
val apps = repository.getApps()

// Prefer StateFlow over LiveData
val pinnedApps: StateFlow<List<AppInfo>> = ...

// Use coroutines for async work
viewModelScope.launch {
    // async work here
}
```

### Compose UI

```kotlin
// Composables are PascalCase
@Composable
fun HomeScreen() { ... }

// Use remember for expensive calculations
val sortedApps = remember(apps) { apps.sortedBy { it.label } }

// Always provide keys for LazyColumn items
LazyColumn {
    items(apps, key = { it.packageName }) { app ->
        AppItem(app)
    }
}

// Extract reusable components
@Composable
private fun AppItem(app: AppInfo) { ... }
```

### Design Tokens

**Always use design tokens from Color.kt**, never hardcode colors:

```kotlin
// ✅ Good
Text(
    text = "App Name",
    color = White90
)

// ❌ Bad
Text(
    text = "App Name",
    color = Color(0xE6FFFFFF)
)
```

### Comments

Comment the **why**, not the **what**:

```kotlin
// ✅ Good
// We intentionally don't show notification badges because they're
// dopamine triggers designed by social media companies
if (!showNotificationBadges) { ... }

// ❌ Bad
// Set showNotificationBadges to false
showNotificationBadges = false
```

### Naming Conventions

- Files: `PascalCase.kt`
- Classes: `PascalCase`
- Functions: `camelCase`
- Variables: `camelCase`
- Constants: `SCREAMING_SNAKE_CASE`
- Composables: `PascalCase`

---

## Pull Request Process

### Before Submitting

1. **Test thoroughly**
   - Run on real device (not just emulator)
   - Test on different Android versions if possible
   - Verify no regressions

2. **Code quality**
   - No compiler warnings
   - Code follows style guide
   - Comments explain non-obvious decisions

3. **Documentation**
   - Update README.md if adding features
   - Add code comments where needed
   - Update CHANGELOG.md

4. **Privacy check** (see below)

### Submitting a PR

1. **Create a feature branch**
   ```bash
   git checkout -b feature/app-drawer-search-improvements
   ```

2. **Make your changes**
   - Keep commits focused and atomic
   - Write clear commit messages
   ```bash
   git commit -m "Improve app drawer search performance

   - Add debounce to search input
   - Use remember for filtering logic
   - Cache search results"
   ```

3. **Push and create PR**
   ```bash
   git push origin feature/app-drawer-search-improvements
   ```
   - Go to GitHub and create Pull Request
   - Fill out the PR template
   - Link related issues

4. **PR description should include:**
   - What problem does this solve?
   - What changes were made?
   - How to test it?
   - Screenshots (for UI changes)
   - Any breaking changes?

### PR Review Process

1. Automated checks run (build, tests)
2. Code review by maintainers
3. Discussion and potential changes requested
4. Approval and merge

**We'll review:**
- Code quality and style
- Alignment with project philosophy
- Privacy/security implications
- Performance impact
- Test coverage

---

## Privacy & Security Rules

These are **non-negotiable** for FocusPhone:

### ❌ Never Add

- `INTERNET` permission to AndroidManifest.xml
- Network libraries (OkHttp, Retrofit, etc.)
- Analytics SDKs (Firebase, Mixpanel, etc.)
- Crash reporters (Crashlytics, Sentry, etc.)
- Ad SDKs
- Social media SDKs
- Any library that phones home

### ✅ Always Ensure

- All data stored locally (DataStore only)
- No cloud sync features
- No accounts or authentication
- Dependencies are AndroidX only (with rare exceptions)
- New permissions are documented and justified

### Security Checklist for PRs

Before submitting, verify:

- [ ] No `INTERNET` permission added
- [ ] No network calls introduced
- [ ] No third-party SDKs added (beyond AndroidX)
- [ ] Data stored locally only
- [ ] No new permissions (or clearly documented if unavoidable)
- [ ] No obfuscation of intent (code should be readable)

**If your PR adds a dependency:**
- Explain why it's needed
- Verify it doesn't require internet
- Check its own dependencies
- Consider if there's a simpler alternative

---

## Testing Guidelines

### Manual Testing Checklist

Before submitting UI changes, test:

- [ ] Feature works as expected
- [ ] No crashes or freezes
- [ ] Smooth animations (60 FPS)
- [ ] Proper back button behavior
- [ ] Long-press gestures work
- [ ] Search and filtering work
- [ ] Settings persist after app restart
- [ ] Works in different orientations (if applicable)

### Automated Tests

We're building up test coverage. Contributions of tests are welcome!

```kotlin
// Example ViewModel test
@Test
fun `pinning an app adds it to pinned list`() {
    val viewModel = MainViewModel(mockRepo, mockPrefs)
    val app = AppInfo("com.example", "Example")

    viewModel.togglePin(app)

    val pinnedApps = viewModel.pinnedApps.value
    assertTrue(pinnedApps.contains(app))
}
```

---

## Getting Help

**Questions about contributing?**
- Open a GitHub Discussion (Q&A category)
- Tag your PR with "help wanted"
- Ask in existing issues

**Stuck on something?**
- Check CLAUDE.md for detailed project architecture
- Search closed issues for similar problems
- Don't hesitate to ask!

**Want to tackle an issue but not sure where to start?**
- Look for "good first issue" label
- Comment on the issue saying you're interested
- We'll guide you through it

---

## Recognition

Contributors will be recognized in:
- CHANGELOG.md for each release
- GitHub Contributors page
- Special thanks in README.md (for significant contributions)

---

## Development Philosophy

When in doubt, ask:

1. **Does this add value for our users?**
   - If it's just "cool" but not useful → probably no

2. **Does this align with minimalism?**
   - If it adds visual clutter → no
   - If it adds complexity → needs strong justification

3. **Does this respect privacy?**
   - If it requires internet → absolutely no
   - If it collects data → absolutely no

4. **Would Steve Jobs ship this?**
   - If it's not polished → not yet
   - If it feels unfinished → not yet

---

## License

By contributing, you agree that your contributions will be licensed under the MIT License (see LICENSE file).

---

**Thank you for contributing to FocusPhone!**

Together we're building a phone experience that respects users' time, attention, and privacy.

*Questions? Open a Discussion on GitHub.*
