package com.sta.market.domain.usecase

import com.sta.market.domain.model.Email
import com.sta.market.domain.model.LoginResult
import com.sta.market.domain.model.Password
import com.sta.market.domain.repository.LoginRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

private const val TEST_EMAIL = "user@example.com"
private const val TEST_PASSWORD = "example123"
private const val TEST_WRONG_EMAIL = "wrong@example.com"
private const val TEST_WRONG_PASSWORD = "wrong123"

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase

    private val fakeRepository = object : LoginRepository {
        override suspend fun login(email: Email, password: Password): LoginResult {
            return if (email.value == TEST_EMAIL && password.value == TEST_PASSWORD) {
                LoginResult.Success
            } else {
                LoginResult.Failure("Invalid credentials")
            }
        }
    }

    @Before
    // Given
    // create an instance of LoginUseCase with a fakeRepository
    fun setup() {
        loginUseCase = LoginUseCase(fakeRepository)
    }

    @Test
    fun `login with correct credentials returns success`() = runTest {
        val result = loginUseCase(email = Email(TEST_EMAIL), password = Password(TEST_PASSWORD))
        assertEquals(LoginResult.Success, result)
    }

    @Test
    fun `login with incorrect credentials returns failure`() = runTest {
        val result = loginUseCase(email = Email(TEST_WRONG_EMAIL), password = Password(TEST_WRONG_PASSWORD))
        assertEquals(
            LoginResult.Failure("Invalid credentials"),
            result
        )
    }
}
