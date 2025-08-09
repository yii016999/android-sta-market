package com.sta.market.domain.repository

import com.sta.market.domain.model.LoginReq
import com.sta.market.domain.model.LoginResp
import com.sta.market.domain.model.UnauthorizedException
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

const val NETWORK_DELAY = 1000L

@Singleton
class LoginRepositoryImpl @Inject constructor() : LoginRepository {

    // For testing mock data
    private val validAccounts = mapOf(
        "test@example.com" to "Test123456",
        "admin@example.com" to "Admin123456",
        "user@example.com" to "User123456"
    )

    override suspend fun login(req: LoginReq): LoginResp {
        delay(NETWORK_DELAY)

        // Check if the email and password are valid
        return if (validAccounts[req.email] == req.password) {
            // Success: get mock token
            LoginResp(token = generateMockToken(req.email))
        } else {
            // Fail：throw UnauthorizedException
            throw UnauthorizedException("Invalid credentials")
        }
    }

    private fun generateMockToken(email: String): String {
        return "mock_token_${email.hashCode()}_${System.currentTimeMillis()}"
    }
}
