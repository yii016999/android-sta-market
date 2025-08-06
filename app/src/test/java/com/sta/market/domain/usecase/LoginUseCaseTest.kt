package com.sta.market.domain.usecase

import com.sta.market.TestConstants.TEST_EMAIL
import com.sta.market.TestConstants.TEST_PASSWORD
import com.sta.market.TestConstants.TEST_WRONG_EMAIL
import com.sta.market.TestConstants.TEST_WRONG_PASSWORD
import com.sta.market.domain.model.Email
import com.sta.market.domain.model.LoginParam
import com.sta.market.domain.model.Password
import com.sta.market.domain.repository.FakeLoginRepository
import com.sta.market.domain.result.LoginResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private val fakeRepository = FakeLoginRepository()

    @Before
    fun setup() {
        // Reset the repository state before each test
        fakeRepository.shouldFail = false
        fakeRepository.failureType = FakeLoginRepository.FailureType.INVALID_CREDENTIALS
        loginUseCase = LoginUseCase(fakeRepository)
    }

    @Test
    fun `login with correct credentials returns success`() = runTest {
        // Given
        val email = Email(TEST_EMAIL)
        val password = Password(TEST_PASSWORD)
        val loginParam = LoginParam(email, password)

        // When
        val result = loginUseCase(loginParam)

        // Then
        assertTrue(result is LoginResult.Success)
    }

    @Test
    fun `login with incorrect credentials returns invalid credentials`() = runTest {
        // Given
        val email = Email(TEST_WRONG_EMAIL)
        val password = Password(TEST_WRONG_PASSWORD)
        val loginParam = LoginParam(email, password)

        // When
        val result = loginUseCase(loginParam)

        // Then
        assertTrue(result is LoginResult.InvalidCredentials)
    }

    @Test
    fun `login with account locked returns account locked`() = runTest {
        // Given
        fakeRepository.shouldFail = true
        fakeRepository.failureType = FakeLoginRepository.FailureType.ACCOUNT_LOCKED
        val email = Email(TEST_EMAIL)
        val password = Password(TEST_PASSWORD)
        val loginParam = LoginParam(email, password)

        // When
        val result = loginUseCase(loginParam)

        // Then
        assertTrue(result is LoginResult.AccountLocked)
    }

    @Test
    fun `login with network error returns network error`() = runTest {
        // Given
        fakeRepository.shouldFail = true
        fakeRepository.failureType = FakeLoginRepository.FailureType.NETWORK_ERROR
        val email = Email(TEST_EMAIL)
        val password = Password(TEST_PASSWORD)
        val loginParam = LoginParam(email, password)

        // When
        val result = loginUseCase(loginParam)

        // Then
        assertTrue(result is LoginResult.NetworkError)
    }

    @Test
    fun `login with unknown error returns unknown error`() = runTest {
        // Given
        fakeRepository.shouldFail = true
        fakeRepository.failureType = FakeLoginRepository.FailureType.UNKNOWN_ERROR
        val email = Email(TEST_EMAIL)
        val password = Password(TEST_PASSWORD)
        val loginParam = LoginParam(email, password)

        // When
        val result = loginUseCase(loginParam)

        // Then
        assertTrue(result is LoginResult.UnknownError)
    }
}
