package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName


data class Comment(
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("date_time")
    val dateTime: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("ticket_comment_id")
    val ticketCommentId: String?,
    @SerializedName("ticket_id")
    val ticketId: String?,
    @SerializedName("user_type")
    val userType: String?
)