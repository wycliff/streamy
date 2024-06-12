package com.movies.streamy.model.dataSource.network.data.response


import com.google.gson.annotations.SerializedName


data class BuildingsResponse(
    @SerializedName("buildings")
    val buildings: List<Building?>?,
    @SerializedName("community_name")
    val communityName: String?
)

data class Building(
    @SerializedName("building_name")
    val buildingName: String?,
    @SerializedName("building_id")
    val buildingId: String?,
    @SerializedName("building_type")
    val buildingType: String?,
    @SerializedName("no_of_units")
    val noOfUnits: String?,
    @SerializedName("units")
    val units: List<UnitItem?>?
)