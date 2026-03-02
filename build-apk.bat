@echo off
echo ============================================
echo FocusPhone APK Builder
echo ============================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo.
    echo Please install Java JDK 17 or higher from:
    echo https://adoptium.net/temurin/releases/?version=17
    echo.
    echo After installation, restart this script.
    pause
    exit /b 1
)

echo [OK] Java is installed
echo.

REM Check if ANDROID_HOME is set
if "%ANDROID_HOME%"=="" (
    echo [WARNING] ANDROID_HOME environment variable is not set
    echo.
    echo Please install Android Studio OR set ANDROID_HOME to your Android SDK location
    echo.
    echo Option 1: Install Android Studio from https://developer.android.com/studio
    echo Option 2: Set ANDROID_HOME manually in System Environment Variables
    echo.
    echo After setup, create a local.properties file with:
    echo sdk.dir=C:\\Users\\Rav\\AppData\\Local\\Android\\Sdk
    echo (or your actual SDK path)
    echo.
)

REM Try to create local.properties if Android SDK exists in default location
if exist "%USERPROFILE%\AppData\Local\Android\Sdk" (
    echo sdk.dir=%USERPROFILE%\AppData\Local\Android\Sdk > local.properties
    echo [OK] Created local.properties with Android SDK path
    echo.
)

echo Starting Gradle build...
echo This may take 5-15 minutes on first run (downloading dependencies)
echo.

REM Run the Gradle build
call gradlew.bat assembleDebug

if %errorlevel% equ 0 (
    echo.
    echo ============================================
    echo BUILD SUCCESSFUL!
    echo ============================================
    echo.
    echo Your APK is ready at:
    echo app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo Next steps:
    echo 1. Copy app-debug.apk to your Galaxy Flip phone
    echo 2. Enable "Install from Unknown Sources" in Settings
    echo 3. Tap the APK to install
    echo 4. Press Home button and select FocusPhone as your launcher
    echo.

    REM Open the output folder
    explorer app\build\outputs\apk\debug
) else (
    echo.
    echo ============================================
    echo BUILD FAILED
    echo ============================================
    echo.
    echo Common fixes:
    echo 1. Make sure Java JDK 17+ is installed
    echo 2. Install Android Studio OR set up Android SDK
    echo 3. Create local.properties with your SDK path
    echo 4. Run: gradlew.bat clean
    echo 5. Try building again
    echo.
    echo See BUILD_INSTRUCTIONS.md for detailed help
    echo.
)

pause
