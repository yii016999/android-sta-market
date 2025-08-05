package com.sta.market.domain.usecase

import com.sta.market.domain.model.Email
import com.sta.market.domain.model.LoginResult
import com.sta.market.domain.model.Password
import com.sta.market.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend operator fun invoke(email: Email, password: Password): LoginResult {
        return repository.login(email, password)
    }
}
