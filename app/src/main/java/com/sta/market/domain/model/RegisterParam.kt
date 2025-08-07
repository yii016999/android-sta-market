package com.sta.market.domain.model

data class RegisterParam(
    val email: String,
    val password: String,
    val confirmPassword: String
)
