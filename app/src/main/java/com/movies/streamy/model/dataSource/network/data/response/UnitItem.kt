package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName

data class UnitItem(
    @SerializedName("tenant_email")
    val tenantEmail: String?,
    @SerializedName("tenant_name")
    val tenantName: String?,
    @SerializedName("tenant_phone")
    val tenantPhone: String?,
    @SerializedName("tenant_id")
    val tenantId: String?,
    @SerializedName("unit_id")
    val unitId: String,
    @SerializedName("tenant_username")
    val tenantUsername: String?,
    @SerializedName("unit_name")
    val unitName: String?,
    @SerializedName("ocupancy_status")
    val ocupancyStatus: String?
)