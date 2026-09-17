package com.example.checkinn_android.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography
import com.example.checkinn_android.ui.designsystem.components.CheckinnItemModel
import com.example.checkinn_android.ui.designsystem.components.CheckinnsItem
import com.example.checkinn_android.ui.designsystem.components.DSButton
import com.example.checkinn_android.ui.designsystem.components.DSButtonVariant
import com.example.checkinn_android.ui.designsystem.components.DSCard
import com.example.checkinn_android.ui.designsystem.components.DSContentHeader
import com.example.checkinn_android.ui.designsystem.components.DSContentHeaderStyle
import com.example.checkinn_android.ui.designsystem.components.DSMetricCard
import com.example.checkinn_android.ui.designsystem.components.HotelHeaderView
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToShowcase: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(DS.Spacing.sm)
                    ) {
                        Text("Dashboard", style = DSTypography.title, color = DSColors.primaryText)
                        HotelHeaderView(hotelName = state.user?.hotelName ?: "Grand CheckInn")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToShowcase) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Design System Showcase",
                            tint = DSColors.appTint
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DSColors.background
                )
            )
        },
        containerColor = DSColors.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DSColors.background),
            contentAlignment = Alignment.TopCenter
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = DSColors.appTint,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val user = state.user
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = DS.Layout.readableMaxWidth)
                        .verticalScroll(rememberScrollState())
                        .padding(DS.Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(DS.Spacing.lg)
                ) {
                    // Profile Header Card
                    DSCard {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                modifier = Modifier.size(64.dp),
                                shape = CircleShape,
                                color = DSColors.brandSoft
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "User Avatar",
                                        modifier = Modifier.size(36.dp),
                                        tint = DSColors.brand
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(DS.Spacing.md))

                            Column {
                                Text(
                                    text = user?.fullName ?: "Authenticated User",
                                    style = DSTypography.title,
                                    color = DSColors.primaryText
                                )
                                Text(
                                    text = user?.role ?: "Staff Member",
                                    style = DSTypography.callout,
                                    color = DSColors.secondaryText
                                )
                            }
                        }
                    }

                    // Metrics Grid
                    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(DS.Spacing.sm)
                        ) {
                            DSMetricCard(
                                title = "Check-ins",
                                value = "24",
                                icon = Icons.Default.CheckCircle,
                                tint = DSColors.success,
                                modifier = Modifier.weight(1f)
                            )
                            DSMetricCard(
                                title = "Places",
                                value = "8",
                                icon = Icons.Default.LocationOn,
                                tint = DSColors.info,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(DS.Spacing.sm)
                        ) {
                            DSMetricCard(
                                title = "Pending",
                                value = "3",
                                icon = Icons.Default.DateRange,
                                tint = DSColors.warning,
                                modifier = Modifier.weight(1f)
                            )
                            DSMetricCard(
                                title = "Issues",
                                value = "1",
                                icon = Icons.Default.Warning,
                                tint = DSColors.danger,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Recent Check-in Section (using ported CheckinnsItem component)
                    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
                        DSContentHeader(
                            title = "Recent Check-in",
                            subtitle = "Latest booking request from guest",
                            style = DSContentHeaderStyle.Section
                        )

                        CheckinnsItem(
                            item = CheckinnItemModel(
                                id = 1001,
                                orderNo = "ORD-1001",
                                name = "Raj Sharma",
                                phoneNumber = "+91 98765 43210",
                                checkinDate = "29 Aug 2026",
                                checkoutDate = "31 Aug 2026",
                                roomNo = "101",
                                roomType = "Suite",
                                bookingSource = "Direct App",
                                idProofTypeName = "Passport",
                                status = BookingStatusType.Initiated
                            ),
                            onShowDetails = onNavigateToShowcase
                        )
                    }

                    // Room DB session card
                    DSCard {
                        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
                            DSContentHeader(
                                title = "Local Database (Room) Session",
                                subtitle = "Offline-first cached credentials and state",
                                style = DSContentHeaderStyle.Section
                            )

                            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

                            Spacer(modifier = Modifier.height(DS.Spacing.xxs))

                            Text(
                                text = "Username: ${user?.username ?: "-"}",
                                style = DSTypography.body,
                                color = DSColors.primaryText
                            )
                            Text(
                                text = "Hotel: ${user?.hotelName ?: "Grand CheckInn"}",
                                style = DSTypography.body,
                                color = DSColors.primaryText
                            )
                            if (!user?.hotelAddress.isNullOrBlank()) {
                                Text(
                                    text = "Address: ${user?.hotelAddress}",
                                    style = DSTypography.footnote,
                                    color = DSColors.secondaryText
                                )
                            }
                            Text(
                                text = "Access Token: ${user?.accessToken?.take(20)?.plus("...") ?: "Active"}",
                                style = DSTypography.body,
                                color = DSColors.secondaryText
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = DS.Spacing.xs)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(DS.Icon.sm),
                                    tint = DSColors.success
                                )
                                Spacer(modifier = Modifier.width(DS.Spacing.xxs))
                                Text(
                                    text = if (user?.isLoggedIn == true) "Active Session in Room DB" else "Inactive",
                                    style = DSTypography.caption,
                                    color = DSColors.success
                                )
                            }
                        }
                    }

                    // Showcase Button
                    DSButton(
                        title = "Explore Design System Kit",
                        icon = Icons.Default.Palette,
                        variant = DSButtonVariant.Secondary,
                        action = onNavigateToShowcase
                    )

                    // Logout Button
                    DSButton(
                        title = "Log Out",
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        variant = DSButtonVariant.Destructive,
                        action = { viewModel.onEvent(HomeUiEvent.LogoutClicked) }
                    )
                }
            }
        }
    }
}
