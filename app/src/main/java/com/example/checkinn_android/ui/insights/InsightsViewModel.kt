package com.example.checkinn_android.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.domain.usecase.GetBookingOrdersUseCase
import com.example.checkinn_android.ui.designsystem.DSColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val getBookingOrdersUseCase: GetBookingOrdersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    private var allBookings: List<BookingOrder> = emptyList()

    init {
        loadInsights()
    }

    fun onEvent(event: InsightsUiEvent) {
        when (event) {
            is InsightsUiEvent.FilterSelected -> {
                _uiState.update { it.copy(selectedFilter = event.filter) }
                recalculateMetrics()
            }
            is InsightsUiEvent.Refresh -> loadInsights()
        }
    }

    fun loadInsights() {
        viewModelScope.launch {
            getBookingOrdersUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                    is Resource.Success -> {
                        allBookings = result.data ?: emptyList()
                        _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                        recalculateMetrics()
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                        recalculateMetrics()
                    }
                }
            }
        }
    }

    private fun recalculateMetrics() {
        val filter = _uiState.value.selectedFilter
        val days = filter.daysCount
        val totalCount = allBookings.size

        val approvedCount = allBookings.count { it.status == BookingStatusType.Approved }
        val deniedCount = allBookings.count { it.status == BookingStatusType.Rejected }
        val checkedOutCount = allBookings.count { it.status == BookingStatusType.OnHold }
        val initiatedCount = allBookings.count { it.status == BookingStatusType.Initiated }

        val dateFormat = SimpleDateFormat(if (filter == InsightsTimeFilter.Last7Days) "EEE" else "dd MMM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val dailyPoints = mutableListOf<DailyInsightDataPoint>()

        for (i in (days - 1) downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            val dayLabel = dateFormat.format(calendar.time)

            // Approximate distribution for chart rendering
            val dayTotal = (totalCount / days).coerceAtLeast(1)
            dailyPoints.add(
                DailyInsightDataPoint(
                    dayLabel = dayLabel,
                    approved = (approvedCount / days).coerceAtLeast(0),
                    denied = (deniedCount / days).coerceAtLeast(0),
                    checkedOut = (checkedOutCount / days).coerceAtLeast(0),
                    total = dayTotal
                )
            )
        }

        val sum = totalCount.coerceAtLeast(1).toDouble()
        val approvedPct = if (totalCount > 0) (approvedCount / sum) * 100 else 0.0
        val deniedPct = if (totalCount > 0) (deniedCount / sum) * 100 else 0.0
        val checkedOutPct = if (totalCount > 0) (checkedOutCount / sum) * 100 else 0.0
        val initiatedPct = if (totalCount > 0) (initiatedCount / sum) * 100 else 0.0

        val distPoints = mutableListOf(
            StatusDistributionDataPoint("Approved", approvedCount, approvedPct, androidx.compose.ui.graphics.Color.Unspecified),
            StatusDistributionDataPoint("Denied", deniedCount, deniedPct, androidx.compose.ui.graphics.Color.Unspecified),
            StatusDistributionDataPoint("Checked Out", checkedOutCount, checkedOutPct, androidx.compose.ui.graphics.Color.Unspecified)
        )
        if (initiatedCount > 0) {
            distPoints.add(StatusDistributionDataPoint("Initiated", initiatedCount, initiatedPct, androidx.compose.ui.graphics.Color.Unspecified))
        }

        _uiState.update {
            it.copy(
                totalCheckinnsCount = totalCount,
                approvedCount = approvedCount,
                deniedCount = deniedCount,
                checkedOutCount = checkedOutCount,
                initiatedCount = initiatedCount,
                dailyDataPoints = dailyPoints,
                statusDistributionPoints = distPoints
            )
        }
    }
}
