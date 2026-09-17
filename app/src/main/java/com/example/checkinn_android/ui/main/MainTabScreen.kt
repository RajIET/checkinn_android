package com.example.checkinn_android.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.KingBed
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.User
import com.example.checkinn_android.ui.checkinns.CheckinnsListScreen
import com.example.checkinn_android.ui.checkinns.CheckinnsListViewModel
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography
import com.example.checkinn_android.ui.insights.InsightsScreen
import com.example.checkinn_android.ui.profile.ProfileScreen

enum class AppTab(val title: String, val icon: ImageVector) {
    Checkins("Check-ins", Icons.Default.KingBed),
    Insights("Insights", Icons.Default.BarChart),
    Profile("Profile", Icons.Default.AccountCircle)
}

@Composable
fun MainTabScreen(
    user: User?,
    checkinnsListViewModel: CheckinnsListViewModel,
    onNavigateToDetails: (BookingOrder) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val hotelName = user?.hotelName

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DSColors.surface,
                contentColor = DSColors.appTint
            ) {
                AppTab.entries.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            if (index == 0) {
                                checkinnsListViewModel.loadBookingOrders()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                style = DSTypography.caption
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DSColors.appTint,
                            selectedTextColor = DSColors.appTint,
                            unselectedIconColor = DSColors.secondaryText,
                            unselectedTextColor = DSColors.secondaryText,
                            indicatorColor = DSColors.brandSoft
                        )
                    )
                }
            }
        },
        containerColor = DSColors.background
    ) { innerPadding ->
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            when (selectedTabIndex) {
                0 -> CheckinnsListScreen(
                    viewModel = checkinnsListViewModel,
                    hotelName = hotelName,
                    onNavigateToDetails = onNavigateToDetails
                )
                1 -> InsightsScreen(
                    hotelName = hotelName
                )
                2 -> ProfileScreen(
                    hotelName = hotelName,
                    onNavigateToLogin = onNavigateToLogin
                )
            }
        }
    }
}
