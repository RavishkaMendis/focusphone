
# FocusPhone ProGuard Rules
# Keep Compose-related classes
-keep class androidx.compose.** { *; }
-keep class kotlin.** { *; }

# Keep DataStore
-keep class androidx.datastore.** { *; }

# Keep our data models
-keep class com.focusphone.launcher.data.** { *; }
