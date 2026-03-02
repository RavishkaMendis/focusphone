package com.focusphone.launcher

import android.app.Application

/**
 * FocusPhone Application
 *
 * Privacy guarantee: No analytics, no crash reporters, no tracking SDKs initialized here.
 * This class exists purely to provide app-level context for DataStore.
 */
class FocusPhoneApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Intentionally empty — no third-party SDKs, no telemetry, no phone-home calls.
        // Your data stays on your device.
    }
}
