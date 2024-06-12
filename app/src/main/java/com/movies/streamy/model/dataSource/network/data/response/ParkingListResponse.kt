package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName


data class ParkingListResponse(
    @SerializedName("data")
    val parkingList: List<ParkingObject?>?,
    @SerializedName("status")
    val status: Boolean?
)