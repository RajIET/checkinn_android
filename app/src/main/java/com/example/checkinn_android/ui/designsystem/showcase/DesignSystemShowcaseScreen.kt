package com.example.checkinn_android.ui.designsystem.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography
import com.example.checkinn_android.ui.designsystem.components.AppAlert
import com.example.checkinn_android.ui.designsystem.components.CheckinnItemModel
import com.example.checkinn_android.ui.designsystem.components.CheckinnsItem
import com.example.checkinn_android.ui.designsystem.components.DSAlertDialog
import com.example.checkinn_android.ui.designsystem.components.DSButton
import com.example.checkinn_android.ui.designsystem.components.DSButtonVariant
import com.example.checkinn_android.ui.designsystem.components.DSCard
import com.example.checkinn_android.ui.designsystem.components.DSContentHeader
import com.example.checkinn_android.ui.designsystem.components.DSContentHeaderStyle
import com.example.checkinn_android.ui.designsystem.components.DSMetricCard
import com.example.checkinn_android.ui.designsystem.components.HotelHeaderView
import com.example.checkinn_android.ui.designsystem.components.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemShowcaseScreen(
    onNavigateBack: () -> Unit
) {
    var sampleAlert by remember { mutableStateOf<AppAlert?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Design System", style = DSTypography.sectionTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DSColors.appTint
                        )
                    }
                },
                actions = {
                    HotelHeaderView(hotelName = "Grand CheckInn")
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
                .verticalScroll(rememberScrollState())
                .padding(DS.Spacing.md),
            verticalArrangement = Arrangement.spacedBy(DS.Spacing.xl)
        ) {
            // Header
            DSContentHeader(
                title = "CheckInn UI Kit",
                subtitle = "A production-ready Jetpack Compose foundation for consistent screens, reusable components, accessible type, and semantic colors ported from Checkinn_mobile.",
                style = DSContentHeaderStyle.Hero
            )

            // Hotel Header Component
            ShowcaseSection(
                title = "Hotel Header Badge",
                subtitle = "Capsule badge displaying hotel identity in top bars and content headers."
            ) {
                DSCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(DS.Spacing.md)
                    ) {
                        HotelHeaderView(hotelName = "Grand Palace Hotel")
                        HotelHeaderView(hotelName = "Seaside Resort")
                    }
                }
            }

            // Colors Section
            ShowcaseSection(
                title = "Colors",
                subtitle = "Professional tokens for surfaces, brand, operational accents, and feedback states."
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.lg)) {
                    ColorGroup(
                        title = "Surfaces",
                        swatches = listOf(
                            "Background" to DSColors.background,
                            "Surface" to DSColors.surface,
                            "Elevated" to DSColors.elevatedSurface,
                            "Fill" to DSColors.fill
                        )
                    )

                    ColorGroup(
                        title = "Brand",
                        swatches = listOf(
                            "Brand" to DSColors.brand,
                            "Pressed" to DSColors.brandPressed,
                            "Soft" to DSColors.brandSoft,
                            "Navy" to DSColors.navy
                        )
                    )

                    ColorGroup(
                        title = "Accents",
                        swatches = listOf(
                            "Indigo" to DSColors.indigo,
                            "Teal" to DSColors.teal,
                            "Cyan" to DSColors.cyan,
                            "Mint" to DSColors.mint,
                            "Amber" to DSColors.amber,
                            "Rose" to DSColors.rose,
                            "Slate" to DSColors.slate,
                            "Stone" to DSColors.stone
                        )
                    )

                    ColorGroup(
                        title = "Status",
                        swatches = listOf(
                            "Success" to DSColors.success,
                            "Warning" to DSColors.warning,
                            "Danger" to DSColors.danger,
                            "Info" to DSColors.info
                        )
                    )
                }
            }

            // Typography Section
            ShowcaseSection(
                title = "Typography",
                subtitle = "System typography that respects Android accessibility and platform conventions."
            ) {
                DSCard {
                    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)) {
                        Text("Hero / Large Title", style = DSTypography.hero, color = DSColors.primaryText)
                        Text("Title / Section Emphasis", style = DSTypography.title, color = DSColors.primaryText)
                        Text("Section Title", style = DSTypography.sectionTitle, color = DSColors.primaryText)
                        Text("Body copy for general content and descriptions.", style = DSTypography.body, color = DSColors.primaryText)
                        Text("Secondary supporting text for metadata and helper copy.", style = DSTypography.callout, color = DSColors.secondaryText)
                        Text("Caption / Metadata", style = DSTypography.caption, color = DSColors.tertiaryText)
                    }
                }
            }

            // Buttons Section
            ShowcaseSection(
                title = "Buttons",
                subtitle = "Primary, secondary, destructive, and low-emphasis actions."
            ) {
                DSCard {
                    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                        DSButton(title = "Primary Button", variant = DSButtonVariant.Primary) {}
                        DSButton(title = "Secondary Button", variant = DSButtonVariant.Secondary) {}
                        DSButton(title = "Ghost Button", variant = DSButtonVariant.Ghost) {}
                        DSButton(title = "Delete", variant = DSButtonVariant.Destructive, icon = Icons.Default.Delete) {}
                        DSButton(title = "Add Guest", variant = DSButtonVariant.Primary, icon = Icons.Default.Add) {}
                        DSButton(title = "Loading", variant = DSButtonVariant.Primary, isLoading = true) {}
                        DSButton(title = "Disabled", variant = DSButtonVariant.Primary, isDisabled = true) {}
                    }
                }
            }

            // Cards & Metrics Section
            ShowcaseSection(
                title = "Cards & Metrics",
                subtitle = "Reusable containers for grouped content and compact overview metrics."
            ) {
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
            }

            // Booking Check-in Card Section
            ShowcaseSection(
                title = "Check-in Card Component",
                subtitle = "Full check-in guest item with avatar gradient, status badge, details, and action button."
            ) {
                CheckinnsItem(
                    item = CheckinnItemModel(
                        id = 1002,
                        orderNo = "ORD-1002",
                        name = "Priya Patel",
                        phoneNumber = "+91 99887 76655",
                        checkinDate = "30 Aug 2026",
                        checkoutDate = "02 Sep 2026",
                        roomNo = "204",
                        roomType = "Deluxe King",
                        bookingSource = "MakeMyTrip",
                        idProofTypeName = "Aadhaar Card",
                        status = BookingStatusType.Approved
                    ),
                    onShowDetails = {}
                )
            }

            // Status Badges Section
            ShowcaseSection(
                title = "Booking Status Badges",
                subtitle = "Official booking order status tags matching Checkinn_mobile."
            ) {
                DSCard {
                    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                        StatusBadge(status = BookingStatusType.Initiated)
                        StatusBadge(status = BookingStatusType.Approved)
                        StatusBadge(status = BookingStatusType.Rejected)
                        StatusBadge(status = BookingStatusType.OnHold)
                    }
                }
            }

            // Alerts & Modals Section
            ShowcaseSection(
                title = "Alerts & Dialogs",
                subtitle = "Native modal alerts matching iOS AppAlert implementation."
            ) {
                DSCard {
                    DSButton(
                        title = "Trigger Sample Alert",
                        icon = Icons.Default.Notifications,
                        variant = DSButtonVariant.Secondary,
                        action = {
                            sampleAlert = AppAlert(
                                title = "CheckInn Notice",
                                message = "This alert component matches the iOS AppAlert implementation exactly.",
                                buttonTitle = "Understood"
                            )
                        }
                    )
                }
            }

            // Spacing Section
            ShowcaseSection(
                title = "Spacing",
                subtitle = "A predictable scale for layout rhythm and component density."
            ) {
                DSCard {
                    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                        SpacingRow("XS", DS.Spacing.xs)
                        SpacingRow("SM", DS.Spacing.sm)
                        SpacingRow("MD", DS.Spacing.md)
                        SpacingRow("LG", DS.Spacing.lg)
                        SpacingRow("XL", DS.Spacing.xl)
                        SpacingRow("XXL", DS.Spacing.xxl)
                    }
                }
            }
        }

        DSAlertDialog(
            alert = sampleAlert,
            onDismissRequest = { sampleAlert = null }
        )
    }
}

@Composable
private fun ShowcaseSection(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
        Column(modifier = Modifier.padding(horizontal = DS.Spacing.xs)) {
            Text(title, style = DSTypography.sectionTitle, color = DSColors.primaryText)
            Spacer(modifier = Modifier.height(DS.Spacing.xxs))
            Text(subtitle, style = DSTypography.caption, color = DSColors.secondaryText)
        }
        content()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorGroup(
    title: String,
    swatches: List<Pair<String, Color>>
) {
    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
        Text(
            text = title.uppercase(),
            style = DSTypography.caption,
            color = DSColors.secondaryText
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DS.Spacing.sm),
            verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm),
            maxItemsInEachRow = 2
        ) {
            swatches.forEach { (name, color) ->
                DSCard(modifier = Modifier.weight(1f)) {
                    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(color, RoundedCornerShape(DS.Radius.md))
                                .border(DS.Stroke.hairline, DSColors.separator.copy(alpha = 0.5f), RoundedCornerShape(DS.Radius.md))
                        )
                        Text(name, style = DSTypography.bodyEmphasized, color = DSColors.primaryText)
                    }
                }
            }
        }
    }
}

@Composable
private fun SpacingRow(name: String, value: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = DSTypography.caption,
            color = DSColors.secondaryText,
            modifier = Modifier.width(44.dp)
        )
        Box(
            modifier = Modifier
                .width(value * 3)
                .height(DS.Spacing.sm)
                .background(DSColors.appTint, RoundedCornerShape(DS.Radius.xs))
        )
        Spacer(modifier = Modifier.width(DS.Spacing.sm))
        Text(
            text = "${value.value.toInt()} dp",
            style = DSTypography.caption,
            color = DSColors.tertiaryText
        )
    }
}
