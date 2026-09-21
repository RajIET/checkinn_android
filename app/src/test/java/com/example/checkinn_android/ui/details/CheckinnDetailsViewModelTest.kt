package com.example.checkinn_android.ui.details

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.data.local.dao.UserDao
import com.example.checkinn_android.data.local.entity.PermissionEntity
import com.example.checkinn_android.data.local.entity.UserEntity
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatus
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.domain.model.Customer
import com.example.checkinn_android.domain.repository.BookingRepository
import com.example.checkinn_android.domain.usecase.CheckPermissionUseCase
import com.example.checkinn_android.domain.usecase.DownloadIdProofUseCase
import com.example.checkinn_android.domain.usecase.UpdateBookingStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private class FakeBookingRepository : BookingRepository {
    var lastUpdatedBookingId: Int? = null
    var lastUpdatedStatusId: Int? = null
    var lastUpdatedNoOfGuest: Int? = null
    var lastUpdatedRoomNumber: String? = null
    var updateCalledCount: Int = 0

    /** Records every (bookingOrderId, imageId) pair passed to downloadIdProof. */
    val downloadIdProofCallArgs = mutableListOf<Pair<Int, Int>>()

    override fun getBookingOrders(): Flow<Resource<List<BookingOrder>>> =
        flowOf(Resource.Success(emptyList()))

    override suspend fun updateBookingStatus(
        bookingId: Int,
        statusId: Int,
        noOfGuest: Int?,
        roomNumber: String?,
        checkoutDate: String?
    ): Flow<Resource<Unit>> {
        updateCalledCount++
        lastUpdatedBookingId = bookingId
        lastUpdatedStatusId = statusId
        lastUpdatedNoOfGuest = noOfGuest
        lastUpdatedRoomNumber = roomNumber
        return flowOf(Resource.Success(Unit))
    }

    override suspend fun downloadIdProof(id: Int, imageId: Int): Flow<Resource<ByteArray>> {
        downloadIdProofCallArgs.add(Pair(id, imageId))
        return flowOf(Resource.Success(ByteArray(0)))
    }
}

private class FakeUserDao : UserDao {
    private val permissions = listOf(
        PermissionEntity(1, "Approve Check-In", "Checkin", "Checkin"),
        PermissionEntity(2, "Reject Check-In", "Checkin", "Checkin")
    )

    override fun getLoggedInUser(): Flow<UserEntity?> = flowOf(null)
    override suspend fun insertUser(user: UserEntity) {}
    override suspend fun deleteAllUsers() {}
    override suspend fun clearUserSession() {}
    override suspend fun updateTokens(accessToken: String?, refreshToken: String?) {}
    override suspend fun insertPermissions(permissions: List<PermissionEntity>) {}
    override fun getPermissions(): Flow<List<PermissionEntity>> = flowOf(permissions)
    override suspend fun deleteAllPermissions() {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class CheckinnDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeBookingRepository: FakeBookingRepository
    private lateinit var viewModel: CheckinnDetailsViewModel

    /**
     * Base booking used by most tests. idProofImageCount is null so the ViewModel
     * defaults to 1 image call (imageId=1) — same behaviour as before this change.
     */
    private val testBooking = BookingOrder(
        id = 101,
        orderNo = "ORD101",
        externalBookingId = null,
        bookingSource = "Direct",
        hotelId = 1,
        roomNo = null,
        customerId = 1,
        roomFloor = 1,
        roomType = "Deluxe",
        specialRequest = null,
        checkinDate = "2026-09-18",
        modifiedOn = null,
        checkoutDate = "2026-09-19",
        createdOn = "2026-09-18",
        idProofTypeId = 1,
        idProofNo = "ID123",
        idProofImagePath = null,
        idProofImageCount = null, // defaults to 1 image
        statusId = 1,
        bookingStatus = BookingStatus(1, BookingStatusType.Initiated),
        customer = Customer(
            customerId = 1,
            customerMobile = "9876543210",
            customerEmail = "test@example.com",
            customerAddress = "123 Street",
            customerIdNumber = "ID123",
            customerCity = "City",
            customerResident = "Resident",
            customerName = "John Doe"
        )
    )

    private fun buildViewModel() {
        val updateBookingStatusUseCase = UpdateBookingStatusUseCase(fakeBookingRepository)
        val downloadIdProofUseCase = DownloadIdProofUseCase(fakeBookingRepository)
        val checkPermissionUseCase = CheckPermissionUseCase(FakeUserDao())

        viewModel = CheckinnDetailsViewModel(
            updateBookingStatusUseCase = updateBookingStatusUseCase,
            downloadIdProofUseCase = downloadIdProofUseCase,
            checkPermissionUseCase = checkPermissionUseCase
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeBookingRepository = FakeBookingRepository()
        buildViewModel()
        viewModel.initBooking(testBooking)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ─── ID Proof image loading ───────────────────────────────────────────────

    @Test
    fun testIdProofSingleImage_makesOneApiCallWithImageIdOne() {
        val booking = testBooking.copy(idProofImageCount = 1)
        fakeBookingRepository = FakeBookingRepository()
        buildViewModel()
        viewModel.initBooking(booking)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            "Expected exactly 1 downloadIdProof call for idProofImageCount=1",
            1,
            fakeBookingRepository.downloadIdProofCallArgs.size
        )
        assertEquals(
            "Expected imageId=1 for the first (and only) call",
            Pair(booking.id, 1),
            fakeBookingRepository.downloadIdProofCallArgs[0]
        )
    }

    @Test
    fun testIdProofTwoImages_makesTwoApiCallsWithCorrectImageIds() {
        val booking = testBooking.copy(idProofImageCount = 2)
        fakeBookingRepository = FakeBookingRepository()
        buildViewModel()
        viewModel.initBooking(booking)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            "Expected exactly 2 downloadIdProof calls for idProofImageCount=2",
            2,
            fakeBookingRepository.downloadIdProofCallArgs.size
        )
        val calledImageIds = fakeBookingRepository.downloadIdProofCallArgs
            .map { it.second }
            .sorted()
        assertEquals(
            "Expected imageId=1 and imageId=2 to be called",
            listOf(1, 2),
            calledImageIds
        )
        fakeBookingRepository.downloadIdProofCallArgs.forEach { (id, _) ->
            assertEquals("All calls must use the booking's id", booking.id, id)
        }
    }

    // ─── Approve / Deny / Room number ────────────────────────────────────────

    @Test
    fun testApproveFailsWhenRoomNumberIsBlank() {
        viewModel.onEvent(CheckinnDetailsUiEvent.RoomNumberChanged(""))
        viewModel.onEvent(CheckinnDetailsUiEvent.ApproveCheckin)

        val state = viewModel.uiState.value
        assertNotNull(state)
        assertEquals("Room number is required", state?.roomNumberError)
        assertEquals("Room Number Required", state?.alert?.title)
        assertEquals(0, fakeBookingRepository.updateCalledCount)
    }

    @Test
    fun testApproveSucceedsWhenRoomNumberIsProvided() {
        viewModel.onEvent(CheckinnDetailsUiEvent.RoomNumberChanged("204"))
        viewModel.onEvent(CheckinnDetailsUiEvent.ApproveCheckin)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state)
        assertNull(state?.roomNumberError)
        assertEquals(1, fakeBookingRepository.updateCalledCount)
        assertEquals(101, fakeBookingRepository.lastUpdatedBookingId)
        assertEquals(3, fakeBookingRepository.lastUpdatedStatusId)
        assertEquals("204", fakeBookingRepository.lastUpdatedRoomNumber)
        assertEquals(BookingStatusType.Approved, state?.status)
    }

    @Test
    fun testDenySucceedsWithoutRoomNumber() {
        viewModel.onEvent(CheckinnDetailsUiEvent.DenyCheckin)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state)
        assertEquals(1, fakeBookingRepository.updateCalledCount)
        assertEquals(101, fakeBookingRepository.lastUpdatedBookingId)
        assertEquals(2, fakeBookingRepository.lastUpdatedStatusId)
        assertNull(fakeBookingRepository.lastUpdatedRoomNumber)
        assertEquals(BookingStatusType.Rejected, state?.status)
    }

    @Test
    fun testRoomNumberChangedClearsError() {
        viewModel.onEvent(CheckinnDetailsUiEvent.RoomNumberChanged(""))
        viewModel.onEvent(CheckinnDetailsUiEvent.ApproveCheckin)
        assertEquals("Room number is required", viewModel.uiState.value?.roomNumberError)

        viewModel.onEvent(CheckinnDetailsUiEvent.RoomNumberChanged("102"))
        assertNull(viewModel.uiState.value?.roomNumberError)
    }
}
