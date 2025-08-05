package com.sta.market.domain.repository

import com.sta.market.domain.model.Email
import com.sta.market.domain.model.LoginResult
import com.sta.market.domain.model.Password

interface LoginRepository {
    suspend fun login(email: Email, password: Password): LoginResult
}
