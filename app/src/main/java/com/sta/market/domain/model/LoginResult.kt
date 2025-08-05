package com.sta.market.domain.model

sealed class LoginResult {
    object Success : LoginResult()
    data class Failure(val message: String) : LoginResult()
}
