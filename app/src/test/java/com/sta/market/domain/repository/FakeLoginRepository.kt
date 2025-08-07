package com.sta.market.domain.repository

import com.sta.market.TestConstants.TEST_EMAIL
import com.sta.market.TestConstants.TEST_ID
import com.sta.market.TestConstants.TEST_PASSWORD
import com.sta.market.domain.model.LoginParam
import com.sta.market.domain.result.LoginResult

class FakeLoginRepository : LoginRepository {

    var shouldFail: Boolean = false
    var failureType: FailureType = FailureType.INVALID_CREDENTIALS

    enum class FailureType {
        INVALID_CREDENTIALS,
        ACCOUNT_LOCKED,
        NETWORK_ERROR,
        UNKNOWN_ERROR
    }

    override suspend fun login(loginParam: LoginParam): LoginResult {
        // Fake network delay
        kotlinx.coroutines.delay(1000)

        return when {
            shouldFail -> when (failureType) {
                FailureType.INVALID_CREDENTIALS -> LoginResult.InvalidCredentials
                FailureType.ACCOUNT_LOCKED -> LoginResult.AccountLocked
                FailureType.NETWORK_ERROR -> LoginResult.NetworkError("Fake Network Error")
                FailureType.UNKNOWN_ERROR -> LoginResult.UnknownError("Fake Unknown Error")
            }

            loginParam.email == TEST_EMAIL && loginParam.password == TEST_PASSWORD -> {
                LoginResult.Success(TEST_ID)
            }

            else -> LoginResult.InvalidCredentials
        }
    }
}
