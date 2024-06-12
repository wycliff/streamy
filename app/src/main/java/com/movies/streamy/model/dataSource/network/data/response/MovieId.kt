package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName

data class MovieId(
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("id")
    val id: String?
)