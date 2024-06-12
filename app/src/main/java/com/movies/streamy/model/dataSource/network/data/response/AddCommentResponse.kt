package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName


data class AddCommentResponse(
    @SerializedName("data")
    val `data`: String?,
    @SerializedName("status")
    val status: Boolean?
)