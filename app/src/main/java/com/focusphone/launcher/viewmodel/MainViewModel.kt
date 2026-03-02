package com.focusphone.launcher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.focusphone.launcher.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * MainViewModel
 *
 * Single source of truth for all app state.
 * MVVM pattern — ViewModel holds state, UI observes and reacts.
 *
 * State flows:
 *   - installedApps: all apps on device
 *   - pinnedApps: apps shown on home screen
 *   - searchQuery: app drawer search filter
 *   - filteredApps: installedApps filtered by search
 *   - focusSession: current focus mode state
 *   - dailyIntention: today's focus intention
 *   - currentTime: live clock (updates every second)
 *   - currentDate: formatted date string
 *   - onboardingComplete: whether to show onboarding
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)
    private val repo  = AppRepository(application)

    // ── App Lists ─────────────────────────────────────────────────────────────

    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()

    private val _isLoadingApps = MutableStateFlow(true)
    val isLoadingApps: StateFlow<Boolean> = _isLoadingApps.asStateFlow()

    // ── Search ────────────────────────────────────────────────────────────────

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredApps: StateFlow<List<AppInfo>> = combine(
        _allApps, _searchQuery
    ) { apps, query ->
        if (query.isBlank()) apps
        else apps.filter { it.appName.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ── Preferences ───────────────────────────────────────────────────────────

    val pinnedPackages:     StateFlow<Set<String>> = prefs.pinnedApps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val mindfulPackages:    StateFlow<Set<String>> = prefs.mindfulDelayApps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val focusAllowedApps:   StateFlow<Set<String>> = prefs.focusAllowedApps
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val mindfulDelaySeconds: StateFlow<Int> = prefs.mindfulDelaySeconds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 5)

    val onboardingComplete: StateFlow<Boolean> = prefs.onboardingComplete
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val focusModeActive: StateFlow<Boolean> = prefs.focusModeActive
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val focusDurationMinutes: StateFlow<Int> = prefs.focusDurationMinutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 25)

    val showClockSeconds: StateFlow<Boolean> = prefs.showClockSeconds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    // ── Derived: pinned apps with full AppInfo ─────────────────────────────────

    val pinnedApps: StateFlow<List<AppInfo>> = combine(
        _allApps, pinnedPackages, mindfulPackages, focusAllowedApps
    ) { apps, pinned, mindful, focusAllowed ->
        apps
            .filter { it.packageName in pinned }
            .map { it.copy(
                isPinned = true,
                hasMindfulDelay = it.packageName in mindful,
                isFocusAllowed = it.packageName in focusAllowed
            ) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // ── Daily Intention ───────────────────────────────────────────────────────

    private val _intentionText = MutableStateFlow("")
    val intentionText: StateFlow<String> = _intentionText.asStateFlow()

    private val _showIntentionPrompt = MutableStateFlow(false)
    val showIntentionPrompt: StateFlow<Boolean> = _showIntentionPrompt.asStateFlow()

    // ── Focus Session Timer ───────────────────────────────────────────────────

    private val _focusSecondsRemaining = MutableStateFlow(0)
    val focusSecondsRemaining: StateFlow<Int> = _focusSecondsRemaining.asStateFlow()

    private var focusTimerJob: Job? = null

    // ── Live Clock ────────────────────────────────────────────────────────────

    private val _currentTime = MutableStateFlow(getCurrentTimeString(false))
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _currentDate = MutableStateFlow(getCurrentDateString())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    // ── Mindful delay overlay ─────────────────────────────────────────────────

    private val _mindfulDelayTarget = MutableStateFlow<AppInfo?>(null)
    val mindfulDelayTarget: StateFlow<AppInfo?> = _mindfulDelayTarget.asStateFlow()

    // ── Init ──────────────────────────────────────────────────────────────────

    init {
        loadApps()
        startClock()
        checkDailyIntention()
    }

    // ── App Loading ───────────────────────────────────────────────────────────

    fun loadApps() {
        viewModelScope.launch {
            _isLoadingApps.value = true
            val apps = repo.getInstalledApps()
            _allApps.value = apps
            _isLoadingApps.value = false
        }
    }

    // ── Clock ─────────────────────────────────────────────────────────────────

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                val showSecs = showClockSeconds.value
                _currentTime.value = getCurrentTimeString(showSecs)
                _currentDate.value = getCurrentDateString()
                delay(1_000)
            }
        }
    }

    private fun getCurrentTimeString(showSeconds: Boolean): String {
        val fmt = if (showSeconds) "HH:mm:ss" else "HH:mm"
        return SimpleDateFormat(fmt, Locale.getDefault()).format(Date())
    }

    private fun getCurrentDateString(): String {
        return SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
    }

    // ── Search ────────────────────────────────────────────────────────────────

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    // ── App Launch ────────────────────────────────────────────────────────────

    /**
     * Attempt to launch an app.
     * If the app has mindful delay enabled, show the countdown overlay first.
     * If focus mode is active and app isn't allowed, block it.
     */
    fun onAppTap(app: AppInfo): LaunchResult {
        // Check focus mode block
        if (focusModeActive.value && app.packageName !in focusAllowedApps.value) {
            return LaunchResult.BlockedByFocus
        }

        // Check mindful delay
        if (app.packageName in mindfulPackages.value) {
            _mindfulDelayTarget.value = app
            return LaunchResult.MindfulDelayRequired(app)
        }

        // Launch directly
        val success = repo.launchApp(app.packageName)
        return if (success) LaunchResult.Launched else LaunchResult.Failed
    }

    fun launchAfterDelay(packageName: String) {
        _mindfulDelayTarget.value = null
        repo.launchApp(packageName)
    }

    fun cancelMindfulDelay() {
        _mindfulDelayTarget.value = null
    }

    // ── Pinning ───────────────────────────────────────────────────────────────

    fun togglePin(packageName: String) {
        viewModelScope.launch {
            val isPinned = packageName in pinnedPackages.value
            prefs.togglePinnedApp(packageName, !isPinned)
        }
    }

    fun toggleMindfulDelay(packageName: String) {
        viewModelScope.launch {
            val hasDelay = packageName in mindfulPackages.value
            prefs.toggleMindfulDelay(packageName, !hasDelay)
        }
    }

    fun toggleFocusAllowed(packageName: String) {
        viewModelScope.launch {
            val isAllowed = packageName in focusAllowedApps.value
            prefs.setFocusAllowedApp(packageName, !isAllowed)
        }
    }

    // ── Daily Intention ───────────────────────────────────────────────────────

    private fun checkDailyIntention() {
        viewModelScope.launch {
            prefs.dailyIntention.collect { intention ->
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                _intentionText.value = intention.text
                if (intention.dateStamp != today && onboardingComplete.value) {
                    _showIntentionPrompt.value = true
                }
            }
        }
    }

    fun saveIntention(text: String) {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            prefs.setDailyIntention(text, today)
            _intentionText.value = text
            _showIntentionPrompt.value = false
        }
    }

    fun skipIntention() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            prefs.setDailyIntention("", today)
            _showIntentionPrompt.value = false
        }
    }

    // ── Focus Mode ────────────────────────────────────────────────────────────

    fun startFocusMode(durationMinutes: Int) {
        viewModelScope.launch {
            prefs.setFocusModeActive(true)
            prefs.setFocusDuration(durationMinutes)
            _focusSecondsRemaining.value = durationMinutes * 60

            focusTimerJob?.cancel()
            focusTimerJob = launch {
                while (_focusSecondsRemaining.value > 0) {
                    delay(1_000)
                    _focusSecondsRemaining.value--
                }
                endFocusMode()
            }
        }
    }

    fun endFocusMode() {
        focusTimerJob?.cancel()
        viewModelScope.launch {
            prefs.setFocusModeActive(false)
            _focusSecondsRemaining.value = 0
        }
    }

    // ── Settings ──────────────────────────────────────────────────────────────

    fun setMindfulDelaySeconds(seconds: Int) {
        viewModelScope.launch { prefs.setMindfulDelaySeconds(seconds) }
    }

    fun setShowClockSeconds(show: Boolean) {
        viewModelScope.launch { prefs.setShowClockSeconds(show) }
    }

    fun completeOnboarding() {
        viewModelScope.launch { prefs.setOnboardingComplete() }
    }
}

sealed class LaunchResult {
    object Launched : LaunchResult()
    object Failed : LaunchResult()
    object BlockedByFocus : LaunchResult()
    data class MindfulDelayRequired(val app: AppInfo) : LaunchResult()
}
