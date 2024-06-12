package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName

data class Tenant(
    val email: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    val tenant_id: String?,
    @SerializedName("unit_name")
    val unitName: String?,
    @SerializedName("username")
    val username: String?
)