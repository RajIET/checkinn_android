package com.example.checkinn_android.ui.insights

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatus
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.domain.repository.BookingRepository
import com.example.checkinn_android.domain.usecase.GetBookingOrdersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

private class FakeBookingRepository : BookingRepository {
    override fun getBookingOrders(): Flow<Resource<List<BookingOrder>>> {
        val orders = listOf(
            BookingOrder(
                id = 1,
                orderNo = "ORD-1",
                externalBookingId = null,
                bookingSource = "App",
                hotelId = 100,
                roomNo = "101",
                customerId = 1,
                roomFloor = 1,
                roomType = "Deluxe",
                specialRequest = null,
                checkinDate = "2026-09-01T14:00:00.000",
                modifiedOn = null,
                checkoutDate = "2026-09-02T11:00:00.000",
                createdOn = "2026-09-01T10:00:00.000",
                idProofTypeId = 1,
                idProofNo = "1234",
                idProofImagePath = null,
                statusId = 3,
                bookingStatus = BookingStatus(3, BookingStatusType.Approved),
                customer = null
            ),
            BookingOrder(
                id = 2,
                orderNo = "ORD-2",
                externalBookingId = null,
                bookingSource = "App",
                hotelId = 100,
                roomNo = "102",
                customerId = 2,
                roomFloor = 1,
                roomType = "Deluxe",
                specialRequest = null,
                checkinDate = "2026-09-02T14:00:00.000",
                modifiedOn = null,
                checkoutDate = "2026-09-03T11:00:00.000",
                createdOn = "2026-09-02T10:00:00.000",
                idProofTypeId = 1,
                idProofNo = "5678",
                idProofImagePath = null,
                statusId = 2,
                bookingStatus = BookingStatus(2, BookingStatusType.Rejected),
                customer = null
            )
        )
        return flowOf(Resource.Success(orders))
    }

    override suspend fun updateBookingStatus(
        bookingId: Int,
        statusId: Int,
        noOfGuest: Int?,
        roomNumber: String?,
        checkoutDate: String?
    ): Flow<Resource<Unit>> = flowOf(Resource.Success(Unit))

    override suspend fun downloadIdProof(id: Int, imageId: Int): Flow<Resource<ByteArray>> =
        flowOf(Resource.Success(ByteArray(0)))
}

@OptIn(ExperimentalCoroutinesApi::class)
class InsightsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: InsightsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val repository = FakeBookingRepository()
        val getBookingOrdersUseCase = GetBookingOrdersUseCase(repository)
        viewModel = InsightsViewModel(getBookingOrdersUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadInsightsCalculatesMetricsCorrectly() {
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(2, state.totalCheckinnsCount)
        assertEquals(1, state.approvedCount)
        assertEquals(1, state.deniedCount)
        assertEquals(0, state.checkedOutCount)
    }

    @Test
    fun testFilterSelectionUpdatesState() {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(InsightsUiEvent.FilterSelected(InsightsTimeFilter.Last30Days))
        assertEquals(InsightsTimeFilter.Last30Days, viewModel.uiState.value.selectedFilter)
    }
}
