package com.movies.streamy.model.dataSource.network.data.request


import com.google.gson.annotations.SerializedName

data class TicketRequest(
    @SerializedName("security_guard_id")
    val securityGuardId: String?,
    @SerializedName("ticket_description")
    val ticketDescription: String?
)