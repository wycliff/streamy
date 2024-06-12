package com.movies.streamy.model.dataSource.network.data.request


data class SignInRequest(
    val security_guard: SecurityGuard?,
    val tenant: Tenant?,
    val vehicle: Vehicle?,
    val visitor: Visitor?
)