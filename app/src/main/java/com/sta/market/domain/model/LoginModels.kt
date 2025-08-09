package com.sta.market.domain.model

data class LoginReq(
    val email: String,
    val password: String,
)

data class LoginResp(
    val token: String
)
