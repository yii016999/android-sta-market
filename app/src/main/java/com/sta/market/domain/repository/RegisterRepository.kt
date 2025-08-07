package com.sta.market.domain.repository

import com.sta.market.domain.model.RegisterParam
import com.sta.market.domain.result.RegisterResult

interface RegisterRepository {
    suspend fun register(registerParam: RegisterParam): RegisterResult
}
