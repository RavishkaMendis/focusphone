# Quick Start - Build FocusPhone APK

## Fastest Method: Use Android Studio

### Step 1: Install Android Studio
1. Download: https://developer.android.com/studio
2. Run the installer (default settings are fine)
3. Launch Android Studio
4. Let it download SDK components (automatic)

### Step 2: Open Project
1. In Android Studio: **File → Open**
2. Select this folder: `c:\Users\Rav\OneDrive\Documents\Kraios\Claude Code\focusphone`
3. Click **OK**
4. Wait for "Gradle sync" to finish (5-10 minutes first time)

### Step 3: Build APK
1. Click **Build** (top menu)
2. Click **Build Bundle(s) / APK(s)**
3. Click **Build APK(s)**
4. Wait 1-3 minutes
5. Click **locate** in the success notification

### Step 4: Install on Phone
1. Copy `app-debug.apk` to your Galaxy Flip
2. On phone: Settings → Security → Install Unknown Apps → Enable for Files/Chrome
3. Open the APK file and tap Install
4. Press Home button → Select **FocusPhone** → **Always**

**Done!** Your privacy-first launcher is now active.

---

## Alternative: Command Line Build

### Prerequisites
1. **Java JDK 17+**: https://adoptium.net/temurin/releases/?version=17
2. **Android Studio** (for SDK) OR manual SDK setup

### Build Command
```cmd
cd "c:\Users\Rav\OneDrive\Documents\Kraios\Claude Code\focusphone"
build-apk.bat
```

The script will:
- Check prerequisites
- Build the APK
- Open the output folder
- Show next steps

---

## Troubleshooting

**"Java is not installed"**
→ Install Java JDK 17: https://adoptium.net/temurin/releases/?version=17

**"SDK location not found"**
→ Install Android Studio (easiest) or create `local.properties`:
```properties
sdk.dir=C:\\Users\\Rav\\AppData\\Local\\Android\\Sdk
```

**Build fails**
→ Run: `gradlew.bat clean` then try `build-apk.bat` again

**Still stuck?**
→ See detailed instructions in `BUILD_INSTRUCTIONS.md`

---

## What You're Building

**FocusPhone** - A privacy-first minimalist launcher
- ✅ Zero data collection
- ✅ No internet permission
- ✅ Text-only interface (no distracting icons)
- ✅ Built for focus and productivity
- ✅ Perfect for Galaxy Flip

**APK Output:** `app\build\outputs\apk\debug\app-debug.apk`
**Size:** ~3-5 MB
**Works on:** Android 8.0+ (including your Galaxy Flip)

---

## Why This Build System?

The Gradle wrapper (`gradlew.bat`) is Android's standard build tool. It:
- Downloads the correct Gradle version automatically
- Manages all dependencies
- Compiles Kotlin code
- Packages everything into an APK

**First build:** 5-15 minutes (downloads everything)
**Subsequent builds:** 1-2 minutes (uses cache)

---

Ready to build? **Just run `build-apk.bat`** (after installing Java + Android Studio)
