package com.sta.market.domain.repository

import com.sta.market.domain.model.LoginReq
import com.sta.market.domain.model.LoginResp

interface LoginRepository {
    // Temporary use domain model, When join api model, change to domain model: suspend fun login(req): RepoResult<LoginResp>
    suspend fun login(req: LoginReq): LoginResp
}
