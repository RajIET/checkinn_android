package com.example.checkinn_android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.ui.checkinns.CheckinnsListViewModel
import com.example.checkinn_android.ui.designsystem.showcase.DesignSystemShowcaseScreen
import com.example.checkinn_android.ui.details.CheckinnDetailsScreen
import com.example.checkinn_android.ui.home.HomeViewModel
import com.example.checkinn_android.ui.login.LoginScreen
import com.example.checkinn_android.ui.main.MainTabScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    var selectedBooking by remember { mutableStateOf<BookingOrder?>(null) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            // ViewModels scoped to this back-stack entry — only created after
            // the user has successfully logged in and tokens are in storage.
            val checkinnsListViewModel: CheckinnsListViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val homeState by homeViewModel.uiState.collectAsState()

            // Trigger the first booking-orders fetch now that we are on the
            // main screen and the access token is guaranteed to be in storage.
            LaunchedEffect(Unit) {
                checkinnsListViewModel.loadBookingOrders()
            }

            MainTabScreen(
                user = homeState.user,
                checkinnsListViewModel = checkinnsListViewModel,
                onNavigateToDetails = { booking ->
                    selectedBooking = booking
                    navController.navigate(Screen.CheckinnDetails.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.CheckinnDetails.route) {
            selectedBooking?.let { booking ->
                CheckinnDetailsScreen(
                    booking = booking,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Screen.DesignSystemShowcase.route) {
            DesignSystemShowcaseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
