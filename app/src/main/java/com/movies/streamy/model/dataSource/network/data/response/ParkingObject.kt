package com.movies.streamy.model.dataSource.network.data.response


import com.google.gson.annotations.SerializedName

data class ParkingObject(
    @SerializedName("amount_due")
    val amountDue: Int?,
    @SerializedName("amount_paid")
    val amountPaid: Int?,
    @SerializedName("balance")
    val balance: Int?,
    @SerializedName("community_name")
    val communityName: String?,
    @SerializedName("number_plate")
    val numberPlate: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("paid_status")
    val paidStatus: String?,
    @SerializedName("park_id")
    val parkId: Int?,
    @SerializedName("parking_paid")
    val parkingPaid: Boolean?,
    @SerializedName("payment_log_description")
    val paymentLogDescription: Any?,
    @SerializedName("readable_time_spent")
    val readableTimeSpent: String?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("time_in")
    val timeIn: String?,
    @SerializedName("time_out")
    val timeOut: String?,
    @SerializedName("time_paid")
    val timePaid: String?,
    @SerializedName("time_spent")
    val timeSpent: Int?,
    @SerializedName("time_to_leave")
    val timeToLeave: String?
)