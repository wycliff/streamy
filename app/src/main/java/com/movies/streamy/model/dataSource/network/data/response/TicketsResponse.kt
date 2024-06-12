package com.movies.streamy.model.dataSource.network.data.response


import com.google.gson.annotations.SerializedName

data class TicketsResponse(
    @SerializedName("data")
    val `data`: List<TicketsItem?>?,
    @SerializedName("status")
    val status: Boolean?
)