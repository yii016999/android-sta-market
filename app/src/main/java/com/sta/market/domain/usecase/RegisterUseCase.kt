package com.sta.market.domain.usecase

import com.sta.market.domain.model.RegisterParam
import com.sta.market.domain.repository.RegisterRepository
import com.sta.market.domain.result.RegisterResult

class RegisterUseCase(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(registerParam: RegisterParam): RegisterResult {

        if (!isValidEmail(registerParam.email)) {
            return RegisterResult.InvalidEmail
        }

        if (registerParam.password != registerParam.confirmPassword) {
            return RegisterResult.PasswordMismatch
        }

        return registerRepository.register(registerParam)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.matches(emailPattern.toRegex())
    }
}
