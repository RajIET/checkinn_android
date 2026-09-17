package com.example.checkinn_android.domain.model

enum class BookingStatusType(val rawValue: String) {
    Initiated("Checkin_Initiated"),
    Rejected("Checkin_Declined"),
    Approved("Checkin_Approved"),
    OnHold("Check on Hold");

    val sortPriority: Int
        get() = when (this) {
            Initiated -> 0
            Approved -> 1
            Rejected -> 2
            OnHold -> 3
        }

    val title: String
        get() = when (this) {
            Initiated -> "Initiated"
            Rejected -> "Request Denied"
            Approved -> "Checkin Approved"
            OnHold -> "Check on Hold"
        }

    companion object {
        fun fromRawValue(value: String?): BookingStatusType {
            return entries.firstOrNull { it.rawValue.equals(value, ignoreCase = true) } ?: Initiated
        }

        fun fromStatusId(id: Int?): BookingStatusType {
            return when (id) {
                1 -> Initiated
                2 -> Rejected
                3 -> Approved
                4 -> OnHold
                else -> Initiated
            }
        }
    }
}

data class BookingStatus(
    val statusId: Int?,
    val orderStatus: BookingStatusType?
)

data class Customer(
    val customerId: Int?,
    val customerMobile: String?,
    val customerEmail: String?,
    val customerAddress: String?,
    val customerIdNumber: String?,
    val customerCity: String?,
    val customerResident: String?,
    val customerName: String?
)

data class BookingOrder(
    val id: Int,
    val orderNo: String,
    val externalBookingId: String?,
    val bookingSource: String?,
    val hotelId: Int,
    val roomNo: String?,
    val customerId: Int?,
    val roomFloor: Int?,
    val roomType: String?,
    val specialRequest: String?,
    val checkinDate: String?,
    val modifiedOn: String?,
    val checkoutDate: String?,
    val createdOn: String?,
    val idProofTypeId: Int?,
    val idProofNo: String?,
    val idProofImagePath: String?,
    val statusId: Int?,
    val bookingStatus: BookingStatus?,
    val customer: Customer?
) {
    val status: BookingStatusType
        get() = bookingStatus?.orderStatus ?: BookingStatusType.fromStatusId(statusId)

    val idProofTypeName: String
        get() = when (idProofTypeId) {
            1 -> "Aadhaar Card"
            2 -> "Passport"
            3 -> "Driving License"
            4 -> "Voter ID"
            else -> "Government ID"
        }
}
