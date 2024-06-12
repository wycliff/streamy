package com.movies.streamy.model.dataSource.network.data.request

data class SignOutVisitorRequest(
    val security_guard_id: Int?,
    val visit_id: Int?,
    val comment: String?
)