package com.movies.streamy.model.domain

import com.google.gson.annotations.SerializedName

data class LocationMetadata(
    @SerializedName("accuracy")
    val accuracy: Double?,
    @SerializedName("active")
    val active: Boolean?,
    @SerializedName("altitude")
    val altitude: Double?,
    @SerializedName("bearing")
    val bearing: Double?,
    @SerializedName("carrier_type")
    val carrierType: Int?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("deviceId")
    val deviceId: Int?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lng")
    val lng: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone_no")
    val phoneNo: String?,
    @SerializedName("rider_stat")
    val riderStat: Int?,
    @SerializedName("driver_id")
    val driverId: String?,
    @SerializedName("speed")
    val speed: Double?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("vendor_type")
    val vendorType: Int?,

)