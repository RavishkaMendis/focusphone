# FocusPhone - Build Instructions

## Prerequisites

To build the FocusPhone APK, you need:

1. **Java Development Kit (JDK) 17 or higher**
2. **Android SDK** (or Android Studio which includes it)

## Option 1: Build with Android Studio (Recommended - Easiest)

This is the simplest method for building the APK.

### Steps:

1. **Install Android Studio**
   - Download from: https://developer.android.com/studio
   - Install with default settings
   - Launch Android Studio and let it download the Android SDK

2. **Open the Project**
   - Open Android Studio
   - Click "Open" (not "New Project")
   - Navigate to this folder: `c:\Users\Rav\OneDrive\Documents\Kraios\Claude Code\focusphone`
   - Click OK

3. **Wait for Gradle Sync**
   - Android Studio will automatically sync Gradle dependencies
   - This may take 5-10 minutes the first time
   - Watch the bottom status bar for progress

4. **Build the APK**
   - Click `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - Wait for the build to complete (1-3 minutes)
   - A notification will appear with "locate" link

5. **Find Your APK**
   - Click the "locate" link in the notification, OR
   - Navigate to: `app\build\outputs\apk\debug\app-debug.apk`

6. **Transfer to Your Phone**
   - Copy `app-debug.apk` to your Galaxy Flip phone
   - Enable "Install from Unknown Sources" in phone Settings
   - Tap the APK file to install

---

## Option 2: Build from Command Line

If you prefer using the command line or don't want to install Android Studio:

### Prerequisites:

1. **Install Java JDK 17**
   - Download: https://adoptium.net/temurin/releases/?version=17
   - Choose Windows x64 installer
   - Install and verify: Open Command Prompt and type `java -version`

2. **Install Android Command Line Tools**
   - Download: https://developer.android.com/studio#command-line-tools-only
   - Extract to: `C:\Android\cmdline-tools`
   - Set environment variables:
     ```
     ANDROID_HOME = C:\Android
     PATH += %ANDROID_HOME%\cmdline-tools\latest\bin
     PATH += %ANDROID_HOME%\platform-tools
     ```

3. **Install Android SDK Components**
   ```cmd
   sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0"
   sdkmanager --licenses
   ```

### Build Steps:

1. **Open Command Prompt**
   ```cmd
   cd "c:\Users\Rav\OneDrive\Documents\Kraios\Claude Code\focusphone"
   ```

2. **Download Gradle Wrapper JAR** (one-time setup)

   The gradle wrapper needs a JAR file. Download it manually:
   - Visit: https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar
   - Save to: `gradle\wrapper\gradle-wrapper.jar`

   OR use PowerShell:
   ```powershell
   Invoke-WebRequest -Uri "https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar" -OutFile "gradle\wrapper\gradle-wrapper.jar"
   ```

3. **Make gradlew executable** (if on Linux/Mac)
   ```bash
   chmod +x gradlew
   ```

4. **Build the Debug APK**
   ```cmd
   gradlew.bat assembleDebug
   ```

   This will:
   - Download Gradle (first time only)
   - Download all dependencies
   - Compile the app
   - Generate the APK

   **Expected time:** 5-15 minutes first run, 1-2 minutes subsequent builds

5. **Locate the APK**
   ```
   app\build\outputs\apk\debug\app-debug.apk
   ```

---

## Option 3: Use Pre-built GitHub Actions (Future)

Once you push this to GitHub, you can set up GitHub Actions to automatically build APKs on every commit.

---

## Troubleshooting

### "JAVA_HOME is not set"
- Install JDK 17 and set environment variable:
  ```
  JAVA_HOME = C:\Program Files\Eclipse Adoptium\jdk-17.x.x
  ```

### "SDK location not found"
- Create `local.properties` file in project root:
  ```properties
  sdk.dir=C:\\Android\\sdk
  ```
  (Use your actual Android SDK path)

### "Could not download gradle-wrapper.jar"
- Manually download from: https://services.gradle.org/distributions/gradle-8.7-bin.zip
- Extract and copy `lib/gradle-wrapper.jar` to `gradle/wrapper/`

### Build fails with "Compilation error"
- Run: `gradlew.bat clean`
- Then try: `gradlew.bat assembleDebug` again

---

## APK Details

**Output Location:** `app\build\outputs\apk\debug\app-debug.apk`

**APK Info:**
- Package: `com.focusphone.launcher`
- Version: 1.0.0 (versionCode 1)
- Min SDK: 26 (Android 8.0)
- Target SDK: 35 (Android 15)
- Architecture: Universal (works on all devices including Galaxy Flip)

**APK Size:** Approximately 3-5 MB

---

## Installing on Your Galaxy Flip

1. **Enable Developer Options** (if not already enabled)
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - Developer Options will now appear in Settings

2. **Enable USB Debugging**
   - Settings → Developer Options → USB Debugging → ON

3. **Transfer APK**

   **Method A: USB Transfer**
   - Connect phone to PC via USB
   - Copy `app-debug.apk` to phone's Download folder
   - Disconnect USB
   - Open "Files" or "My Files" app on phone
   - Navigate to Downloads
   - Tap `app-debug.apk`
   - Tap "Install"

   **Method B: Install Directly via ADB**
   - Connect phone via USB
   - Run: `adb install app\build\outputs\apk\debug\app-debug.apk`
   - The app will install automatically

4. **Set as Default Launcher**
   - Press the Home button
   - Android will ask "Which launcher do you want to use?"
   - Select "FocusPhone"
   - Choose "Always" to set as default

---

## Quick Start (If you have Android Studio installed)

```
1. Open Android Studio
2. File → Open → Select focusphone folder
3. Wait for sync to complete
4. Build → Build Bundle(s) / APK(s) → Build APK(s)
5. Click "locate" in the notification
6. Transfer app-debug.apk to your phone
7. Install and enjoy!
```

---

## Need Help?

If you encounter issues:
1. Check the Troubleshooting section above
2. Google the specific error message
3. The Android Studio build output shows detailed error information

The project is correctly configured and should build without issues once you have Java and Android SDK installed.
