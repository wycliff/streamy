package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName

data class VisitsItem(
    @SerializedName("building_name")
    val buildingName: String?,
    @SerializedName("no_of_occupants")
    val noOfOccupants: String?,
    @SerializedName("sign_in_comment")
    val signInComment: String?,
    @SerializedName("sign_in_guard")
    val signInGuard: SignInGuard?,
    @SerializedName("sign_in_time")
    val signInTime: String?,
    @SerializedName("sign_out_comment")
    val signOutComment: String?,
    @SerializedName("sign_out_guard")
    val signOutGuard: SignOutGuard?,
    @SerializedName("sign_out_time")
    val signOutTime: String?,
    @SerializedName("tenant")
    val tenant: Tenant?,
    @SerializedName("vehicle_color")
    val vehicleColor: String?,
    @SerializedName("vehicle_reg_no")
    val vehicleRegNo: String?,
    @SerializedName("vehicle_type")
    val vehicleType: String?,
    @SerializedName("visit_id")
    val visitId: String?,
    @SerializedName("visitor_email")
    val visitorEmail: String?,
    @SerializedName("visitor_id_no")
    val visitorIdNo: String?,
    @SerializedName("visitor_name")
    val visitorName: String?,
    @SerializedName("visitor_phone")
    val visitorPhone: String?
)