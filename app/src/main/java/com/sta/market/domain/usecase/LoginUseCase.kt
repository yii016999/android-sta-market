package com.sta.market.domain.usecase

import com.sta.market.domain.model.LoginParam
import com.sta.market.domain.repository.LoginRepository
import com.sta.market.domain.result.LoginResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend operator fun invoke(loginParam: LoginParam): LoginResult {
        return repository.login(loginParam)
    }
}
