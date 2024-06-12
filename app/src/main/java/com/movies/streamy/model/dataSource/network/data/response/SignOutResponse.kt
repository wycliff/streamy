package com.movies.streamy.model.dataSource.network.data.response


import com.google.gson.annotations.SerializedName

data class SignOutResponse(
    @SerializedName("data")
    val message: String?,
    @SerializedName("status")
    val status: Boolean?
)