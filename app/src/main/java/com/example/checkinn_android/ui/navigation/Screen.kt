package com.example.checkinn_android.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Main : Screen("main")
    data object CheckinnDetails : Screen("checkinn_details")
    data object DesignSystemShowcase : Screen("design_system_showcase")
}
