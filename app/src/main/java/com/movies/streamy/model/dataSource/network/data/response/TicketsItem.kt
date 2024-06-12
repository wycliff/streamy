package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName


data class TicketsItem(
    @SerializedName("date_time")
    val dateTime: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("ticket_description")
    val ticketDescription: String?,
    @SerializedName("ticket_id")
    val ticketId: String?,
    @SerializedName("ticket_status")
    val ticketStatus: String?,
    @SerializedName("user_type")
    val userType: Any?
)