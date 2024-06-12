package com.movies.streamy.model.dataSource.network.data.request

data class LoginRequest(
    val phone: String?,
    val password: String?,
    val role: String?
)