package com.movies.streamy.model.dataSource.network.data.request

import com.google.gson.annotations.SerializedName

data class ParkCheckRequest (
@SerializedName("number_plate")
val numberPlate: String?
)