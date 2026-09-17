package com.example.checkinn_android.ui.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.checkinn_android.domain.model.User
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography
import com.example.checkinn_android.ui.designsystem.components.DSButton
import com.example.checkinn_android.ui.designsystem.components.DSButtonVariant
import com.example.checkinn_android.ui.designsystem.components.DSCard
import com.example.checkinn_android.ui.designsystem.components.HotelHeaderView
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    hotelName: String?,
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProfileUiEffect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DSColors.background)
                .verticalScroll(rememberScrollState())
                .padding(DS.Spacing.md),
            verticalArrangement = Arrangement.spacedBy(DS.Spacing.lg)
        ) {
            state.user?.let { user ->
                UserHeaderCard(user = user)
                UserDetailsCard(user = user)
                HotelDetailsCard(user = user)
            }

            AppInfoCard()
            AccountActionsCard(onSignOut = { viewModel.onEvent(ProfileUiEvent.SignOut) })
        }
    }
}

@Composable
private fun UserHeaderCard(user: User) {
    DSCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(DSColors.brandSoft, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials(forUser = user),
                    style = DSTypography.hero.copy(fontWeight = FontWeight.Bold),
                    color = DSColors.brand
                )
            }

            Spacer(modifier = Modifier.width(DS.Spacing.md))

            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xxs)) {
                Text(
                    text = displayName(forUser = user),
                    style = DSTypography.title,
                    color = DSColors.primaryText
                )

                user.role?.takeIf { it.isNotBlank() }?.let { role ->
                    Box(
                        modifier = Modifier
                            .background(DSColors.brandSoft, shape = CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = role,
                            style = DSTypography.caption.copy(fontWeight = FontWeight.SemiBold),
                            color = DSColors.brand
                        )
                    }
                }

                user.hotelName?.takeIf { it.isNotBlank() }?.let { hotel ->
                    Text(
                        text = hotel,
                        style = DSTypography.footnote,
                        color = DSColors.secondaryText
                    )
                }
            }
        }
    }
}

@Composable
private fun UserDetailsCard(user: User) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)) {
            Text(
                text = "User Details",
                style = DSTypography.sectionTitle,
                color = DSColors.primaryText
            )

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                val name = user.fullName
                if (name.isNotBlank()) {
                    DetailRow(icon = Icons.Default.Person, title = "Name", value = name)
                }
                user.username.takeIf { it.isNotBlank() }?.let { username ->
                    DetailRow(icon = Icons.Default.AlternateEmail, title = "Username", value = username)
                }
            }
        }
    }
}

@Composable
private fun HotelDetailsCard(user: User) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)) {
            Text(
                text = "Hotel Details",
                style = DSTypography.sectionTitle,
                color = DSColors.primaryText
            )

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                user.hotelName?.takeIf { it.isNotBlank() }?.let { hotelName ->
                    DetailRow(icon = Icons.Default.Apartment, title = "Hotel Name", value = hotelName)
                }
                user.hotelRating?.let { rating ->
                    if (rating > 0) {
                        DetailRow(icon = Icons.Default.Star, title = "Hotel Rating", value = "⭐".repeat(rating.coerceAtMost(5)))
                    }
                }
                val cityStateText = listOfNotNull(user.city, user.state).filter { it.isNotBlank() }.joinToString(", ")
                if (cityStateText.isNotBlank()) {
                    DetailRow(icon = Icons.Default.LocationCity, title = "City, State", value = cityStateText)
                }
                user.hotelQRCode?.takeIf { it.isNotBlank() }?.let { qrCode ->
                    DetailRow(icon = Icons.Default.QrCode, title = "Hotel QR Code", value = qrCode)
                }
                user.hotelAddress?.takeIf { it.isNotBlank() }?.let { address ->
                    AddressDetailBlock(icon = Icons.Default.LocationOn, title = "Address", value = address)
                }
            }
        }
    }
}

@Composable
private fun AppInfoCard() {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)) {
            Text(
                text = "App Information",
                style = DSTypography.sectionTitle,
                color = DSColors.primaryText
            )

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                DetailRow(icon = Icons.Default.AppShortcut, title = "App Name", value = "CheckInn")
                DetailRow(icon = Icons.Default.Info, title = "Version", value = "1.0.0")
            }
        }
    }
}

@Composable
private fun AccountActionsCard(onSignOut: () -> Unit) {
    DSButton(
        title = "Sign Out",
        icon = Icons.AutoMirrored.Filled.Logout,
        variant = DSButtonVariant.Destructive,
        action = onSignOut
    )
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DSColors.slate,
                modifier = Modifier.size(DS.Icon.sm)
            )
            Spacer(modifier = Modifier.width(DS.Spacing.sm))
            Text(
                text = title,
                style = DSTypography.body,
                color = DSColors.secondaryText
            )
        }

        Text(
            text = value,
            style = DSTypography.bodyEmphasized,
            color = DSColors.primaryText
        )
    }
}

@Composable
private fun AddressDetailBlock(
    icon: ImageVector,
    title: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DS.Spacing.xxs)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DSColors.slate,
                modifier = Modifier.size(DS.Icon.sm)
            )
            Spacer(modifier = Modifier.width(DS.Spacing.sm))
            Text(
                text = title,
                style = DSTypography.body,
                color = DSColors.secondaryText
            )
        }
        Text(
            text = value,
            style = DSTypography.bodyEmphasized,
            color = DSColors.primaryText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DS.Icon.sm + DS.Spacing.sm)
        )
    }
}

private fun initials(forUser: User): String {
    val first = forUser.firstName.firstOrNull()?.toString() ?: ""
    val last = forUser.lastName.firstOrNull()?.toString() ?: ""
    val combined = "$first$last".uppercase()
    return combined.ifEmpty { forUser.username.firstOrNull()?.uppercase() ?: "U" }
}

private fun displayName(forUser: User): String {
    val name = forUser.fullName
    if (name.isNotBlank()) return name
    if (forUser.username.isNotBlank()) return forUser.username
    return "User Profile"
}
