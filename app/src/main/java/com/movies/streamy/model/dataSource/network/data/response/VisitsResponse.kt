package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName

data class VisitsResponse(
    @SerializedName("data")
    val visitData: VisitsData?,
    @SerializedName("status")
    val status: Boolean?
)

data class VisitsData(
    @SerializedName("community_name")
    val community_name: String?,
    @SerializedName("visit")
    val visit: List<VisitsItem>?
)