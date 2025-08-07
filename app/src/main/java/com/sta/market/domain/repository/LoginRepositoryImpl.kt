package com.sta.market.domain.repository

import com.sta.market.domain.model.LoginParam
import com.sta.market.domain.result.LoginResult
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
) : LoginRepository {

    override suspend fun login(loginParam: LoginParam): LoginResult {
        return try {
            LoginResult.Success("Login Success")
        } catch (e: Exception) {
            LoginResult.UnknownError(e.message ?: "Login Error")
        }
    }
}
