package com.example.checkinn_android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5B89FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E2C52),
    onPrimaryContainer = Color(0xFFE8F0FF),
    secondary = Color(0xFF45C4F5),
    onSecondary = Color.White,
    background = Color(0xFF000000),
    surface = Color(0xFF1C1C1E),
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFF87171),
    onError = Color.White,
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFF9E9EA3)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A4AD4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F0FF),
    onPrimaryContainer = Color(0xFF121F38),
    secondary = Color(0xFF0885B3),
    onSecondary = Color.White,
    background = Color(0xFFF2F2F7),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
    error = Color(0xFFDB2626),
    onError = Color.White,
    surfaceVariant = Color(0xFFF2F2F7),
    onSurfaceVariant = Color(0xFF6E6E73)
)

@Composable
fun CheckinnAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
