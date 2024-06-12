package com.movies.streamy.model.dataSource.network.data.request


import com.google.gson.annotations.SerializedName

data class ParkRequest(
    @SerializedName("number_plate")
    val numberPlate: String?,
    @SerializedName("security_guard_id")
    val securityGuardId: String?
)