package com.sta.market.domain.result

sealed class RegisterResult {
    data class Success(val userId: String) : RegisterResult()
    object InvalidEmail : RegisterResult()
    object PasswordMismatch : RegisterResult()
    object EmailAlreadyExists : RegisterResult()
    data class NetworkError(val message: String) : RegisterResult()
    data class UnknownError(val message: String) : RegisterResult()
}
