package com.example.checkinn_android.ui.checkinns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KingBed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography
import com.example.checkinn_android.ui.designsystem.components.CheckinnsItem
import com.example.checkinn_android.ui.designsystem.components.HotelHeaderView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckinnsListScreen(
    viewModel: CheckinnsListViewModel,
    hotelName: String?,
    onNavigateToDetails: (BookingOrder) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Check-ins",
                        style = DSTypography.title,
                        color = DSColors.primaryText
                    )
                },
                actions = {
                    HotelHeaderView(hotelName = hotelName)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DSColors.background
                )
            )
        },
        containerColor = DSColors.background
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.onEvent(CheckinnsListUiEvent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DSColors.background)
        ) {
            when {
                state.isLoading && state.orders.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = DSColors.brand)
                    }
                }
                state.orders.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(DS.Spacing.md),
                        verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)
                    ) {
                        items(
                            items = state.orders,
                            key = { it.id }
                        ) { booking ->
                            CheckinnsItem(
                                checkin = booking,
                                onShowDetails = { onNavigateToDetails(booking) }
                            )
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(DS.Spacing.xl),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.KingBed,
                                contentDescription = null,
                                tint = DSColors.secondaryText,
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(DS.Spacing.md))

                            Text(
                                text = "No Check-ins",
                                style = DSTypography.title,
                                color = DSColors.primaryText
                            )

                            Spacer(modifier = Modifier.height(DS.Spacing.xs))

                            Text(
                                text = state.errorMessage ?: "No pending check-ins found at the moment.",
                                style = DSTypography.body,
                                color = DSColors.secondaryText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
