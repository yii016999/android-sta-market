package com.sta.market.domain.repository

import com.sta.market.domain.model.LoginParam
import com.sta.market.domain.result.LoginResult

interface LoginRepository {
    suspend fun login(loginParam: LoginParam): LoginResult
}
