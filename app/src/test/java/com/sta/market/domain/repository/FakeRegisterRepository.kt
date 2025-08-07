package com.sta.market.domain.repository

import com.sta.market.TestConstants.TEST_EMAIL
import com.sta.market.TestConstants.TEST_EXISTING_EMAIL
import com.sta.market.TestConstants.TEST_ID
import com.sta.market.domain.model.RegisterParam
import com.sta.market.domain.result.RegisterResult

class FakeRegisterRepository : RegisterRepository {

    var shouldFail: Boolean = false
    var failureType: FailureType = FailureType.EMAIL_ALREADY_EXISTS

    enum class FailureType {
        EMAIL_ALREADY_EXISTS,
        NETWORK_ERROR,
        UNKNOWN_ERROR
    }

    override suspend fun register(registerParam: RegisterParam): RegisterResult {
        // Fake network delay
        kotlinx.coroutines.delay(1000)

        return when {
            shouldFail -> when (failureType) {
                FailureType.EMAIL_ALREADY_EXISTS -> RegisterResult.EmailAlreadyExists
                FailureType.NETWORK_ERROR -> RegisterResult.NetworkError("Fake Network Error")
                FailureType.UNKNOWN_ERROR -> RegisterResult.UnknownError("Fake Unknown Error")
            }

            registerParam.email == TEST_EXISTING_EMAIL -> {
                RegisterResult.EmailAlreadyExists
            }

            registerParam.email == TEST_EMAIL -> {
                RegisterResult.Success(TEST_ID)
            }

            else -> RegisterResult.Success(TEST_ID)
        }
    }
}
