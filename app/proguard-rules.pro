# =============================================================================
#  Checkinn Android — ProGuard / R8 Rules
# =============================================================================
#  Applied only for release builds.
# =============================================================================

# ---------------------------------------------------------------------------
# 1. Stack-trace readability (crash reporting / debug symbols)
# ---------------------------------------------------------------------------
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ---------------------------------------------------------------------------
# 2. Kotlin
# ---------------------------------------------------------------------------
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.Lazy { *; }
-dontwarn kotlin.**

# ---------------------------------------------------------------------------
# 3. Jetpack / AndroidX
# ---------------------------------------------------------------------------
# ViewModel — prevent constructor stripping
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Compose — keep all Composable functions
-keepclasseswithmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Navigation
-keep class androidx.navigation.** { *; }

# ---------------------------------------------------------------------------
# 4. Hilt / Dagger
# ---------------------------------------------------------------------------
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.** { *; }
-keepclasseswithmembers class * {
    @dagger.* <methods>;
    @javax.inject.* <fields>;
    @javax.inject.* <init>(...);
}
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-dontwarn dagger.hilt.**

# ---------------------------------------------------------------------------
# 5. Retrofit + OkHttp + Gson
# ---------------------------------------------------------------------------
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ---------------------------------------------------------------------------
# 6. Project DTOs & Entities (Gson / Room must not rename these)
# ---------------------------------------------------------------------------
-keep class com.example.checkinn_android.data.remote.dto.** { *; }
-keepclassmembers class com.example.checkinn_android.data.remote.dto.** {
    <fields>;
    <init>(...);
}

-keep class com.example.checkinn_android.data.local.entity.** { *; }
-keepclassmembers class com.example.checkinn_android.data.local.entity.** {
    <fields>;
    <init>(...);
}
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

-keep class com.example.checkinn_android.domain.model.** { *; }

# ---------------------------------------------------------------------------
# 7. Security Crypto (EncryptedSharedPreferences / KeyStore)
# ---------------------------------------------------------------------------
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**

# ---------------------------------------------------------------------------
# 8. Coil (image loading)
# ---------------------------------------------------------------------------
-keep class coil.** { *; }
-dontwarn coil.**

# ---------------------------------------------------------------------------
# 9. Coroutines
# ---------------------------------------------------------------------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler { *; }
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# ---------------------------------------------------------------------------
# 10. Security hardening — strip all Log calls in release
# ---------------------------------------------------------------------------
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
    public static int wtf(...);
}

-keepattributes !LocalVariableTable,!LocalVariableTypeTable