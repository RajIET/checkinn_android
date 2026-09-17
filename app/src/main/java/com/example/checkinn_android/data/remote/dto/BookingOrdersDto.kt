package com.example.checkinn_android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BookingResponseDto(
    @SerializedName("data")
    val data: BookingDataDto?,
    @SerializedName("error")
    val error: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
)

data class BookingDataDto(
    @SerializedName("user_Id")
    val userId: String?,
    @SerializedName("hotel_Id")
    val hotelId: Int?,
    @SerializedName("totalRecords")
    val totalRecords: Int?,
    @SerializedName("bookingOrders")
    val bookingOrders: List<BookingOrderDto>?
)

data class BookingOrderDto(
    @SerializedName("booking_ID")
    val id: Int,
    @SerializedName("orderNo")
    val orderNo: String?,
    @SerializedName("external_Booking_ID")
    val externalBookingId: String?,
    @SerializedName("booking_Source")
    val bookingSource: String?,
    @SerializedName("hotel_id")
    val hotelId: Int?,
    @SerializedName("room_no")
    val roomNo: String?,
    @SerializedName("customer_ID")
    val customerId: Int?,
    @SerializedName("room_Floor")
    val roomFloor: Int?,
    @SerializedName("room_Type")
    val roomType: String?,
    @SerializedName("special_request")
    val specialRequest: String?,
    @SerializedName("checkin_Date")
    val checkinDate: String?,
    @SerializedName("modified_on")
    val modifiedOn: String?,
    @SerializedName("checkout_Date")
    val checkoutDate: String?,
    @SerializedName("created_on")
    val createdOn: String?,
    @SerializedName("idProof_typeID")
    val idProofTypeId: Int?,
    @SerializedName("iD_proof_No")
    val idProofNo: String?,
    @SerializedName("idProof_ImagePath")
    val idProofImagePath: String?,
    @SerializedName("status_id")
    val statusId: Int?,
    @SerializedName("booking_Status")
    val bookingStatus: BookingStatusDto?,
    @SerializedName("customer")
    val customer: CustomerDto?
)

data class BookingStatusDto(
    @SerializedName("status_ID")
    val statusId: Int?,
    @SerializedName("order_Status")
    val orderStatus: String?
)

data class CustomerDto(
    @SerializedName("customer_ID")
    val customerId: Int?,
    @SerializedName("customer_Mobile")
    val customerMobile: String?,
    @SerializedName("customer_Email")
    val customerEmail: String?,
    @SerializedName("customer_Address")
    val customerAddress: String?,
    @SerializedName("customer_IDNumber")
    val customerIdNumber: String?,
    @SerializedName("customer_city")
    val customerCity: String?,
    @SerializedName("customer_resident")
    val customerResident: String?,
    @SerializedName("customer_Name")
    val customerName: String?
)

data class UpdateBookingStatusRequestDto(
    @SerializedName("booking_ID")
    val bookingId: Int,
    @SerializedName("status_ID")
    val statusId: Int,
    @SerializedName("no_Of_Guest")
    val noOfGuest: Int?
)
