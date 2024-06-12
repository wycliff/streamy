package com.movies.streamy.model.dataSource.network.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val loginData: LoginData?,
    @SerializedName("jwt")
    val jsonWebToken: String?,
    @SerializedName("status")
    val status: Boolean?
)

data class LoginData(
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("firstName")
    val firstName: String?,
    @SerializedName("id_number")
    val id_number: String?,
    @SerializedName("lastName")
    val lastName: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("securityCompanyCode")
    val securityCompanyCode: String?,
    @SerializedName("securityCompanyId")
    val securityCompanyId: String?,
    @SerializedName("securityCompanyName")
    val securityCompanyName: String?,
    @SerializedName("security_guard_id")
    val security_guard_id: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("username")
    val username: String?
)