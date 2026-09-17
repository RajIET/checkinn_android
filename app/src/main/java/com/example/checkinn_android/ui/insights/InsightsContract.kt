package com.example.checkinn_android.ui.insights

import androidx.compose.ui.graphics.Color
import com.example.checkinn_android.ui.designsystem.DSColors

enum class InsightsTimeFilter(val label: String, val daysCount: Int) {
    Last7Days("Last 7 Days", 7),
    Last30Days("Last 30 Days", 30)
}

data class DailyInsightDataPoint(
    val dayLabel: String,
    val approved: Int,
    val denied: Int,
    val checkedOut: Int,
    val total: Int
)

data class StatusDistributionDataPoint(
    val status: String,
    val count: Int,
    val percentage: Double,
    val color: Color
)

data class InsightsUiState(
    val selectedFilter: InsightsTimeFilter = InsightsTimeFilter.Last7Days,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val totalCheckinnsCount: Int = 0,
    val approvedCount: Int = 0,
    val deniedCount: Int = 0,
    val checkedOutCount: Int = 0,
    val initiatedCount: Int = 0,
    val dailyDataPoints: List<DailyInsightDataPoint> = emptyList(),
    val statusDistributionPoints: List<StatusDistributionDataPoint> = emptyList()
)

sealed interface InsightsUiEvent {
    data class FilterSelected(val filter: InsightsTimeFilter) : InsightsUiEvent
    data object Refresh : InsightsUiEvent
}
