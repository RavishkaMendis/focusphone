# Security Policy

## Our Privacy & Security Guarantee

FocusPhone is built with privacy as the foundation, not an afterthought. This is not marketing speak — it's enforced at the architectural level.

### What Makes FocusPhone Secure

**1. Zero Internet Permission**
- FocusPhone requests **no `INTERNET` permission** in AndroidManifest.xml
- This is not a policy we can break — it's physically impossible for the app to make network calls
- Android's permission system prevents any data from leaving your device
- You can verify this yourself: Search for "INTERNET" in `app/src/main/AndroidManifest.xml`

**2. No Third-Party SDKs**
- Zero analytics libraries (no Google Analytics, no Firebase Analytics)
- Zero crash reporters (no Crashlytics, no Sentry)
- Zero advertising SDKs
- Zero social media SDKs
- The only dependencies are Android Jetpack libraries (Google's official toolkit)

**3. Local-Only Data Storage**
- All app settings stored via Android DataStore on device
- No cloud sync of any kind
- No accounts, no login, no authentication servers
- Your data never leaves your phone

**4. Open Source**
- 100% of the code is public and auditable
- No obfuscation beyond standard release optimization
- Community can verify every claim we make
- See something concerning? Open an issue.

---

## Security Audit Checklist

Want to verify our claims? Here's how:

### 1. Verify No Internet Permission
```bash
# Check AndroidManifest.xml
grep -i "INTERNET" app/src/main/AndroidManifest.xml
# Should return nothing

# Or check compiled APK
aapt dump permissions app-debug.apk | grep INTERNET
# Should return nothing
```

### 2. Verify No Network Calls at Runtime
```bash
# Install app and monitor network activity
adb shell dumpsys connectivity | grep focusphone
# Should show no active connections

# Or use Android Studio's Network Profiler
# No network requests should appear
```

### 3. Verify Dependencies
```bash
# Check all dependencies
cat app/build.gradle.kts
cat gradle/libs.versions.toml

# Compare against our whitelist:
# - androidx.* (official Android libraries only)
# - org.jetbrains.kotlinx:kotlinx-coroutines-android (Kotlin standard library)
# Nothing else should be present
```

### 4. Verify Data Storage
```bash
# Check what files the app creates
adb shell run-as com.focusphone.launcher ls -la files/
adb shell run-as com.focusphone.launcher ls -la databases/

# Should only see DataStore preferences file (datastore/*)
# No SQLite databases, no suspicious files
```

### 5. Source Code Audit
```bash
# Search for any network code
grep -r "HttpClient\|OkHttp\|Retrofit\|URL\|URLConnection" app/src/
# Should return nothing

# Search for analytics
grep -r "analytics\|tracking\|firebase" app/src/
# Should return nothing (except comments explaining absence)
```

---

## Reporting a Security Issue

**We take security seriously.** If you discover a vulnerability:

### What Qualifies as a Security Issue

- Code that could leak user data
- Permission escalation vulnerabilities
- Crashes that could be exploited
- Dependencies with known CVEs
- Any behavior that contradicts our privacy claims

### What's NOT a Security Issue

- Feature requests
- UI/UX bugs that don't affect security
- Performance issues
- Compatibility issues with specific devices

### How to Report

**For sensitive vulnerabilities:**
1. Do NOT open a public GitHub issue
2. Email: [Your email - set this up]
3. Use subject line: "FocusPhone Security Issue"
4. Include:
   - Description of the vulnerability
   - Steps to reproduce
   - Potential impact
   - Your suggested fix (if any)

**For non-critical issues:**
1. Open a GitHub issue with label `security`
2. Include reproduction steps
3. We'll respond within 48 hours

### Response Timeline

- **Critical vulnerabilities** (data leakage, permission bypass): 24 hours
- **High severity** (crashes, DoS): 72 hours
- **Medium/Low severity**: 1 week

### Recognition

Security researchers who responsibly disclose issues will be:
- Credited in CHANGELOG.md (if desired)
- Listed in SECURITY.md Hall of Fame
- Thanked publicly (with permission)

---

## Security Best Practices for Contributors

If you're contributing code:

### Never Add These

❌ Network libraries (OkHttp, Retrofit, etc.)
❌ Analytics SDKs (Firebase, Mixpanel, etc.)
❌ Crash reporters (Crashlytics, Sentry, etc.)
❌ Ad SDKs
❌ Any library that requires `INTERNET` permission

### Always Do This

✅ Store data via DataStore only (no SharedPreferences, no SQLite for user data)
✅ Use AndroidX libraries (official Google support libs)
✅ Document any new permissions in PRs
✅ Explain why any new dependency is needed
✅ Run security audit checklist before submitting PR

### Code Review Focus Areas

Pull requests will be reviewed for:
1. No new permissions added
2. No network calls introduced
3. No third-party SDKs added
4. Data stays local
5. No obfuscation of intent

---

## Privacy Policy Compliance

### GDPR (EU)
- **No data collected** = automatic compliance
- No consent forms needed
- No data retention policies needed
- No right-to-deletion requests (there's nothing to delete)

### CCPA (California)
- **No data sold** = automatic compliance
- No "Do Not Sell My Info" links needed

### Google Play Data Safety
Our Play Store listing will accurately reflect:
- ✅ No data collected
- ✅ No data shared with third parties
- ✅ No data encrypted (because there's nothing to encrypt)
- ✅ Users can request deletion (nothing to delete)

---

## Third-Party Audits

We welcome independent security audits. If you're a security researcher or firm interested in auditing FocusPhone:

1. The entire codebase is open source (see LICENSE)
2. Feel free to decompile our release APKs
3. Monitor network traffic, file access, etc.
4. Publish your findings (we have nothing to hide)

**Found nothing?** We'd love a testimonial for our README!

---

## Threat Model

### What We Protect Against

✅ **Data collection by developer** (us)
- Solution: No internet permission, no backend servers

✅ **Third-party tracking**
- Solution: No third-party SDKs

✅ **Accidental data leakage**
- Solution: No network code whatsoever

✅ **Privacy policy violations**
- Solution: Can't violate what doesn't exist

### What We Don't Protect Against

⚠️ **Device compromises** (malware, rooted devices with malicious apps)
- If your device is compromised, all apps are vulnerable

⚠️ **Physical access attacks**
- If someone has your unlocked phone, they can access any app

⚠️ **Android OS vulnerabilities**
- We rely on Android's permission system working correctly

⚠️ **Network surveillance**
- We don't collect data, but we also don't encrypt device-to-device communications (because there are none)

---

## Security Updates

We monitor:
- AndroidX library updates for security patches
- Kotlin language updates
- Android platform security bulletins
- CVEs in our dependencies

**Update policy:**
- Critical security updates: Emergency release within 48 hours
- High priority updates: Within 1 week
- Routine updates: Monthly maintenance releases

---

## Hall of Fame

Security researchers who have helped make FocusPhone more secure:

*(None yet - be the first!)*

---

## Questions?

**General security questions:** Open a GitHub Discussion
**Potential vulnerability:** Email [Your secure email]
**Want to help audit?** We'd love that - clone and audit away!

---

*Last updated: March 2, 2025*
*Next review: Quarterly*

**Our Promise:** Privacy by architecture, not by policy.
