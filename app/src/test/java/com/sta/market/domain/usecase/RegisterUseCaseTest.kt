package com.sta.market.domain.usecase

import com.sta.market.TestConstants.TEST_CONFIRM_PASSWORD
import com.sta.market.TestConstants.TEST_EMAIL
import com.sta.market.TestConstants.TEST_EXISTING_EMAIL
import com.sta.market.TestConstants.TEST_PASSWORD
import com.sta.market.TestConstants.TEST_WRONG_EMAIL
import com.sta.market.domain.model.RegisterParam
import com.sta.market.domain.repository.FakeRegisterRepository
import com.sta.market.domain.result.RegisterResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegisterUseCaseTest {
    private lateinit var registerUseCase: RegisterUseCase
    private val fakeRepository = FakeRegisterRepository()

    @Before
    fun setup() {
        // Reset the repository state before each test
        fakeRepository.shouldFail = false
        fakeRepository.failureType = FakeRegisterRepository.FailureType.EMAIL_ALREADY_EXISTS
        registerUseCase = RegisterUseCase(fakeRepository)
    }

    @Test
    fun `register with valid credentials returns success`() = runTest {
        // Given
        val email = TEST_EMAIL
        val password = TEST_PASSWORD
        val confirmPassword = TEST_CONFIRM_PASSWORD
        val registerParam = RegisterParam(email, password, confirmPassword)

        // When
        val result = registerUseCase(registerParam)

        // Then
        assertTrue(result is RegisterResult.Success)
    }

    @Test
    fun `register with invalid email format returns invalid email`() = runTest {
        // Given
        val email = TEST_WRONG_EMAIL
        val password = TEST_PASSWORD
        val confirmPassword = TEST_CONFIRM_PASSWORD
        val registerParam = RegisterParam(email, password, confirmPassword)

        // When
        val result = registerUseCase(registerParam)

        // Then
        assertTrue(result is RegisterResult.InvalidEmail)
    }

    @Test
    fun `register with password mismatch returns password mismatch`() = runTest {
        // Given
        val email = TEST_EMAIL
        val password = TEST_PASSWORD
        val confirmPassword = "different_password"
        val registerParam = RegisterParam(email, password, confirmPassword)

        // When
        val result = registerUseCase(registerParam)

        // Then
        assertTrue(result is RegisterResult.PasswordMismatch)
    }

    @Test
    fun `register with existing email returns email already exists`() = runTest {
        // Given
        fakeRepository.shouldFail = true
        fakeRepository.failureType = FakeRegisterRepository.FailureType.EMAIL_ALREADY_EXISTS
        val email = TEST_EXISTING_EMAIL
        val password = TEST_PASSWORD
        val confirmPassword = TEST_CONFIRM_PASSWORD
        val registerParam = RegisterParam(email, password, confirmPassword)

        // When
        val result = registerUseCase(registerParam)

        // Then
        assertTrue(result is RegisterResult.EmailAlreadyExists)
    }

    @Test
    fun `register with network error returns network error`() = runTest {
        // Given
        fakeRepository.shouldFail = true
        fakeRepository.failureType = FakeRegisterRepository.FailureType.NETWORK_ERROR
        val email = TEST_EMAIL
        val password = TEST_PASSWORD
        val confirmPassword = TEST_CONFIRM_PASSWORD
        val registerParam = RegisterParam(email, password, confirmPassword)

        // When
        val result = registerUseCase(registerParam)

        // Then
        assertTrue(result is RegisterResult.NetworkError)
    }

    @Test
    fun `register with unknown error returns unknown error`() = runTest {
        // Given
        fakeRepository.shouldFail = true
        fakeRepository.failureType = FakeRegisterRepository.FailureType.UNKNOWN_ERROR
        val email = TEST_EMAIL
        val password = TEST_PASSWORD
        val confirmPassword = TEST_CONFIRM_PASSWORD
        val registerParam = RegisterParam(email, password, confirmPassword)

        // When
        val result = registerUseCase(registerParam)

        // Then
        assertTrue(result is RegisterResult.UnknownError)
    }
}
