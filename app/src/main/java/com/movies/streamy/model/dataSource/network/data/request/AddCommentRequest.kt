package com.movies.streamy.model.dataSource.network.data.request

import com.google.gson.annotations.SerializedName


data class AddCommentRequest(
    val comment_description: String?,
    @SerializedName("security_guard_id")
    val security_guard_id: String?,
    @SerializedName("ticket_id")
    val ticket_id: String?
)