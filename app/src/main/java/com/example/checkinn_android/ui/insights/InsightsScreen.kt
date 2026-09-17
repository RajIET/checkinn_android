package com.example.checkinn_android.ui.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.KingBed
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography
import com.example.checkinn_android.ui.designsystem.components.DSCard
import com.example.checkinn_android.ui.designsystem.components.HotelHeaderView
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    hotelName: String?,
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Insights",
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
            TimeFilterSegmentControl(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { viewModel.onEvent(InsightsUiEvent.FilterSelected(it)) }
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = DSColors.brand)
                }
            } else {
                SummaryBannerCard(state = state)
                MetricsGridSection(state = state)
                SimpleTrendChartSection(state = state)
                VisualStatusBreakdownSection(state = state)
            }
        }
    }
}

@Composable
private fun TimeFilterSegmentControl(
    selectedFilter: InsightsTimeFilter,
    onFilterSelected: (InsightsTimeFilter) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
        Text(
            text = "Select Time Period",
            style = DSTypography.caption.copy(fontWeight = FontWeight.Bold),
            color = DSColors.secondaryText
        )

        val selectedIndex = InsightsTimeFilter.entries.indexOf(selectedFilter)
        TabRow(
            selectedTabIndex = selectedIndex,
            containerColor = DSColors.surface,
            contentColor = DSColors.brand,
            divider = {}
        ) {
            InsightsTimeFilter.entries.forEachIndexed { index, filter ->
                Tab(
                    selected = selectedIndex == index,
                    onClick = { onFilterSelected(filter) },
                    text = {
                        Text(
                            text = filter.label,
                            style = DSTypography.bodyEmphasized,
                            color = if (selectedIndex == index) DSColors.brand else DSColors.secondaryText
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SummaryBannerCard(state: InsightsUiState) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = DSColors.brand,
                    modifier = Modifier.size(DS.Icon.sm)
                )
                Spacer(modifier = Modifier.width(DS.Spacing.xs))
                Text(
                    text = "At a Glance (${state.selectedFilter.label})",
                    style = DSTypography.bodyEmphasized,
                    color = DSColors.primaryText
                )
            }

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            Text(
                text = "Out of ${state.totalCheckinnsCount} total check-in requests:",
                style = DSTypography.body,
                color = DSColors.secondaryText
            )

            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DS.Spacing.xs)
                ) {
                    StatusChip(
                        text = "${state.approvedCount} Approved",
                        icon = Icons.Default.CheckCircle,
                        color = DSColors.success,
                        background = DSColors.successSoft,
                        modifier = Modifier.weight(1f)
                    )
                    StatusChip(
                        text = "${state.deniedCount} Denied",
                        icon = Icons.Default.Cancel,
                        color = DSColors.danger,
                        background = DSColors.dangerSoft,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(DS.Spacing.xs)
                ) {
                    StatusChip(
                        text = "${state.checkedOutCount} Checked Out",
                        icon = Icons.Default.MeetingRoom,
                        color = DSColors.info,
                        background = DSColors.infoSoft,
                        modifier = Modifier.weight(1f)
                    )
                    StatusChip(
                        text = "${state.initiatedCount} Pending",
                        icon = Icons.Default.Schedule,
                        color = DSColors.warning,
                        background = DSColors.warningSoft,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(
    text: String,
    icon: ImageVector,
    color: Color,
    background: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(background, shape = CircleShape)
            .padding(horizontal = DS.Spacing.xs, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            style = DSTypography.caption.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

@Composable
private fun MetricsGridSection(state: InsightsUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
        Text(
            text = "Summary Cards",
            style = DSTypography.sectionTitle,
            color = DSColors.primaryText
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DS.Spacing.md)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MetricCard(
                    title = "Total Check-ins",
                    subtitle = "All requests",
                    value = "${state.totalCheckinnsCount}",
                    icon = Icons.Default.KingBed,
                    iconColor = DSColors.brand,
                    badgeBackground = DSColors.brandSoft
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                MetricCard(
                    title = "Approved",
                    subtitle = "Allowed check-ins",
                    value = "${state.approvedCount}",
                    icon = Icons.Default.CheckCircle,
                    iconColor = DSColors.success,
                    badgeBackground = DSColors.successSoft
                )
            }
        }

        Spacer(modifier = Modifier.height(DS.Spacing.xs))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DS.Spacing.md)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MetricCard(
                    title = "Denied",
                    subtitle = "Rejected requests",
                    value = "${state.deniedCount}",
                    icon = Icons.Default.Cancel,
                    iconColor = DSColors.danger,
                    badgeBackground = DSColors.dangerSoft
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                MetricCard(
                    title = "Checked Out",
                    subtitle = "Completed stays",
                    value = "${state.checkedOutCount}",
                    icon = Icons.Default.MeetingRoom,
                    iconColor = DSColors.info,
                    badgeBackground = DSColors.infoSoft
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    subtitle: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    badgeBackground: Color
) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xs)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(badgeBackground, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(DS.Icon.sm)
                    )
                }

                Text(
                    text = value,
                    style = DSTypography.hero,
                    color = DSColors.primaryText
                )
            }

            Text(
                text = title,
                style = DSTypography.sectionTitle,
                color = DSColors.primaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                style = DSTypography.footnote,
                color = DSColors.secondaryText
            )
        }
    }
}

@Composable
private fun SimpleTrendChartSection(state: InsightsUiState) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
            Text(
                text = "Daily Trend",
                style = DSTypography.sectionTitle,
                color = DSColors.primaryText
            )

            Text(
                text = "Requests count per day",
                style = DSTypography.footnote,
                color = DSColors.secondaryText
            )

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                state.dailyDataPoints.takeLast(7).forEach { point ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(((point.total * 20).coerceIn(16, 80)).dp)
                                .background(DSColors.brand, shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        )
                        Spacer(modifier = Modifier.height(DS.Spacing.xxs))
                        Text(
                            text = point.dayLabel,
                            style = DSTypography.caption,
                            color = DSColors.secondaryText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VisualStatusBreakdownSection(state: InsightsUiState) {
    DSCard {
        Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.md)) {
            Text(
                text = "Status Breakdown",
                style = DSTypography.sectionTitle,
                color = DSColors.primaryText
            )

            HorizontalDivider(color = DSColors.separator.copy(alpha = 0.5f))

            Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.sm)) {
                state.statusDistributionPoints.forEach { item ->
                    val itemColor = when (item.status) {
                        "Approved" -> DSColors.success
                        "Denied" -> DSColors.danger
                        "Checked Out" -> DSColors.info
                        else -> DSColors.amber
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(DS.Spacing.xxs)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.status,
                                style = DSTypography.bodyEmphasized,
                                color = DSColors.primaryText
                            )
                            Text(
                                text = "${item.count} (${String.format(Locale.US, "%.1f", item.percentage)}%)",
                                style = DSTypography.footnote,
                                color = DSColors.secondaryText
                            )
                        }

                        LinearProgressIndicator(
                            progress = { (item.percentage / 100f).toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = itemColor,
                            trackColor = itemColor.copy(alpha = 0.15f)
                        )
                    }
                }
            }
        }
    }
}
