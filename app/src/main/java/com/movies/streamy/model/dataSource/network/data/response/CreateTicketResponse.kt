package com.movies.streamy.model.dataSource.network.data.response


import com.google.gson.annotations.SerializedName


data class CreateTicketResponse(
    @SerializedName("data")
    val `data`: String?,
    @SerializedName("status")
    val status: Boolean?
)