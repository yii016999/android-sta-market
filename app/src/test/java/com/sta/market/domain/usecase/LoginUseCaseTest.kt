package com.sta.market.domain.usecase

import com.sta.market.TestConstants.TEST_EMAIL
import com.sta.market.TestConstants.TEST_LOCKED_EMAIL
import com.sta.market.TestConstants.TEST_PASSWORD
import com.sta.market.TestConstants.TEST_TOKEN
import com.sta.market.TestConstants.TEST_WRONG_FORMATE_EMAIL
import com.sta.market.TestConstants.TEST_WRONG_PASSWORD
import com.sta.market.domain.model.AccountLockedException
import com.sta.market.domain.model.LoginReq
import com.sta.market.domain.model.LoginResp
import com.sta.market.domain.model.UnauthorizedException
import com.sta.market.domain.repository.LoginRepository
import com.sta.market.domain.result.LoginResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals


class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase

    @MockK
    private lateinit var loginRepository: LoginRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        loginUseCase = LoginUseCase(loginRepository)
    }

    @Test
    fun `login with correct credentials returns Success`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_EMAIL.trim(), TEST_PASSWORD)
        val loginResp = LoginResp(token = TEST_TOKEN)

        coEvery { loginRepository.login(loginReq) } returns loginResp

        // When
        val result = loginUseCase(TEST_EMAIL, TEST_PASSWORD)

        // Then
        assertTrue("Should return Success", result is LoginResult.Success)
        assertEquals(TEST_TOKEN, (result as LoginResult.Success).token)

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `login with empty email returns InvalidCredentials without calling repository`() = runTest {
        // When
        val result = loginUseCase("", TEST_PASSWORD)

        // Then
        assertTrue("Should return InvalidCredentials", result is LoginResult.InvalidCredentials)

        // Verify repository was NOT called
        coVerify(exactly = 0) { loginRepository.login(any()) }
    }

    @Test
    fun `login with UnauthorizedException returns InvalidCredentials`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_WRONG_FORMATE_EMAIL.trim(), TEST_WRONG_PASSWORD)

        coEvery { loginRepository.login(loginReq) } throws
                UnauthorizedException("Invalid credentials")

        // When
        val result = loginUseCase(TEST_WRONG_FORMATE_EMAIL, TEST_WRONG_PASSWORD)

        // Then
        assertTrue("Should return InvalidCredentials", result is LoginResult.InvalidCredentials)

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `login with AccountLockedException returns AccountLocked`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_LOCKED_EMAIL.trim(), TEST_PASSWORD)

        coEvery { loginRepository.login(loginReq) } throws
                AccountLockedException("Account is locked")

        // When
        val result = loginUseCase(TEST_LOCKED_EMAIL, TEST_PASSWORD)

        // Then
        assertTrue("Should return AccountLocked", result is LoginResult.AccountLocked)

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `login with IOException returns NetworkError`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_EMAIL.trim(), TEST_PASSWORD)

        coEvery { loginRepository.login(loginReq) } throws
                IOException("Network connection failed")

        // When
        val result = loginUseCase(TEST_EMAIL, TEST_PASSWORD)

        // Then
        assertTrue("Should return NetworkError", result is LoginResult.NetworkError)

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }
}
