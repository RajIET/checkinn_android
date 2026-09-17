package com.example.checkinn_android.ui.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object DSColors {
    // MARK: - Brand
    val brand = Color(0xFF1A4AD4)
    val brandPressed = Color(0xFF12339E)

    val brandSoft: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF253354) else Color(0xFFE8F0FF)

    val appTint: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF4C82F6) else brand

    // MARK: - Neutrals
    val navy = Color(0xFF121F38)
    val slate: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFA0B1C9) else Color(0xFF4A5C78)
    val stone = Color(0xFF786E63)

    // MARK: - Accents
    val indigo: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF818CF8) else Color(0xFF4F45E6)
    val teal: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF2DD4BF) else Color(0xFF0D9687)
    val cyan: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF0284C7) else Color(0xFF0885B3)
    val mint: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF34D399) else Color(0xFF29A673)
    val amber: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFBBF24) else Color(0xFFDB8205)
    val rose: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFB7185) else Color(0xFFE0386B)

    // MARK: - Semantic (solid)
    val success: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF34D399) else Color(0xFF1A9659)
    val warning: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFBBF24) else Color(0xFFDB8205)
    val danger: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFF87171) else Color(0xFFDB2626)
    val info: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF60A5FA) else Color(0xFF0870D1)

    // MARK: - Semantic (soft — for badges, banners, tags)
    val successSoft: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF0B3C26) else Color(0xFFE3F5E8)
    val warningSoft: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF422307) else Color(0xFFFFF2D9)
    val dangerSoft: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF421518) else Color(0xFFFCE6E6)
    val infoSoft: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF0C3859) else Color(0xFFE3F0FC)

    // Static text & border base tokens
    val linkText: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF60A5FA) else Color(0xFF0870D1)
    val overlay = Color(0x66000000) // 40% black
    val shadow = Color(0x0F000000)  // 6% black

    // Dynamic surfaces based on theme
    val background: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF0E0F14) else Color(0xFFF2F2F7)

    val surface: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF1D1F2B) else Color(0xFFFFFFFF)

    val elevatedSurface: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF282B3C) else Color(0xFFFFFFFF)

    val separator: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF35384B) else Color(0x4A3C3C43)

    val fill: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF2A2D3E) else Color(0x29787880)

    val border: Color
        @Composable get() = secondaryText.copy(alpha = 0.12f)

    val borderStrong: Color
        @Composable get() = secondaryText.copy(alpha = 0.24f)

    // Dynamic text
    val primaryText: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)

    val secondaryText: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFA6ADBB) else Color(0xFF6E6E73)

    val tertiaryText: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0x7A545458) else Color(0x7A3C3C43)

    val disabledText: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0x47545458) else Color(0x473C3C43)

    // Dynamic disabled states
    val disabledBackground: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF282B3C) else Color(0xFFE5E5EA)

    val disabledBorder: Color
        @Composable get() = secondaryText.copy(alpha = 0.08f)
}
