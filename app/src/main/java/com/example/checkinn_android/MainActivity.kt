package com.example.checkinn_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.checkinn_android.core.session.SessionEvent
import com.example.checkinn_android.core.session.SessionManager
import com.example.checkinn_android.data.local.security.SecureTokenStorage
import com.example.checkinn_android.ui.navigation.NavGraph
import com.example.checkinn_android.ui.navigation.Screen
import com.example.checkinn_android.ui.theme.CheckinnAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var secureTokenStorage: SecureTokenStorage

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination = if (secureTokenStorage.hasValidTokens()) {
            Screen.Main.route
        } else {
            Screen.Login.route
        }

        setContent {
            CheckinnAndroidTheme {
                val controller = rememberNavController()

                // Listen for session events (like unauthorized/logout) and route accordingly
                LaunchedEffect(controller) {
                    sessionManager.events.collect { event ->
                        when (event) {
                            is SessionEvent.Unauthorized -> {
                                controller.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    }
                }

                NavGraph(
                    navController = controller,
                    startDestination = startDestination
                )
            }
        }
    }
}
