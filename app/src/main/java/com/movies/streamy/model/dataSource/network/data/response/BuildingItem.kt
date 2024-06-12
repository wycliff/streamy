package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName

data class BuildingItem(
    @SerializedName("building_id")
    val buildingId: String?,
    @SerializedName("building_name")
    val buildingName: String?,
    @SerializedName("units")
    val units : List<UnitItem?>,
    @SerializedName("no_of_units")
    val noOfUnits: String?
)