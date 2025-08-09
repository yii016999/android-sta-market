package com.sta.market.domain.result

sealed class LoginResult {
    data class Success(val token: String) : LoginResult()
    data object InvalidCredentials : LoginResult()
    data object AccountLocked : LoginResult()
    data class NetworkError(val message: String) : LoginResult()
    data class UnknownError(val message: String) : LoginResult()
}
