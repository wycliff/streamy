package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("data")
    val message: String?,
)

