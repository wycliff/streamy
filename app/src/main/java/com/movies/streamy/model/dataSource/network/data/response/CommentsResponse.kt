package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName


data class CommentsResponse(
    @SerializedName("data")
    val comments: List<Comment?>?,
    @SerializedName("status")
    val status: Boolean?
)