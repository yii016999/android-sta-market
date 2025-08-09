package com.sta.market.presentation.login

import com.sta.market.TestConstants.TEST_EMAIL
import com.sta.market.TestConstants.TEST_PASSWORD
import com.sta.market.TestConstants.TEST_TOKEN
import com.sta.market.TestConstants.TEST_WRONG_EMAIL
import com.sta.market.TestConstants.TEST_WRONG_PASSWORD
import com.sta.market.domain.model.AccountLockedException
import com.sta.market.domain.model.LoginReq
import com.sta.market.domain.model.LoginResp
import com.sta.market.domain.model.UnauthorizedException
import com.sta.market.domain.repository.LoginRepository
import com.sta.market.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginRepository: LoginRepository
    private lateinit var loginUseCase: LoginUseCase

    // Use StandardTestDispatcher for better control of coroutines timing
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Initialize mock repository with relaxed mode for better debugging
        loginRepository = mockk(relaxed = true)
        loginUseCase = LoginUseCase(loginRepository)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState should be Idle`() {
        // When & Then
        val currentState = viewModel.uiState.value
        assertTrue(currentState is LoginUiState.Idle)
    }

    @Test
    fun `when login starts, uiState should go from Idle to Loading to Success`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_EMAIL.trim(), TEST_PASSWORD)
        val loginResp = LoginResp(token = TEST_TOKEN)
        val states = mutableListOf<LoginUiState>()

        coEvery { loginRepository.login(loginReq) } coAnswers {
            // Small delay to ensure Loading state is captured
            kotlinx.coroutines.delay(100)
            loginResp
        }

        // Collect the states
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        // When
        viewModel.login(TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        job.cancel()

        // Then
        assertTrue(
            "Should have at least 3 states: Idle, Loading, Success, but got ${states.size} states: $states",
            states.size >= 3
        )
        assertTrue(
            "Initial state should be Idle, but was ${states[0]}",
            states[0] is LoginUiState.Idle
        )
        assertTrue(
            "Second state should be Loading, but was ${states[1]}",
            states[1] is LoginUiState.Loading
        )
        assertTrue(
            "Final state should be Success, but was ${states.last()}",
            states.last() is LoginUiState.Success
        )

        // Verify repository was called
        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `when login success, uiState should be Success`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_EMAIL.trim(), TEST_PASSWORD)
        val loginResp = LoginResp(token = TEST_TOKEN)

        // Mock successful login
        coEvery { loginRepository.login(loginReq) } returns loginResp

        // When
        viewModel.login(TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Success state", currentState is LoginUiState.Success)

        // Verify repository was called with correct parameters
        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `when login with wrong credentials, uiState should be Error with invalid credentials message`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_WRONG_EMAIL.trim(), TEST_WRONG_PASSWORD)

        // Mock repository throw UnauthorizedException
        coEvery { loginRepository.login(loginReq) } throws UnauthorizedException("Invalid credentials")

        // When
        viewModel.login(TEST_WRONG_EMAIL, TEST_WRONG_PASSWORD)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Error state", currentState is LoginUiState.Error)
        val errorState = currentState as LoginUiState.Error
        assertEquals("Account or Password is not correct", errorState.message)

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `when account is locked, uiState should be Error with account locked message`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_EMAIL.trim(), TEST_PASSWORD)

        // Mock repository throw AccountLockedException
        coEvery { loginRepository.login(loginReq) } throws AccountLockedException("Account is locked")

        // When
        viewModel.login(TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Error state", currentState is LoginUiState.Error)
        val errorState = currentState as LoginUiState.Error
        assertEquals(
            "Your account has been locked, Please contact the administrator",
            errorState.message
        )

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `when network error occurs, uiState should be Error with network error message`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_EMAIL.trim(), TEST_PASSWORD)

        // Mock repository throw IOException
        coEvery { loginRepository.login(loginReq) } throws IOException("Network connection failed")

        // When
        viewModel.login(TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Error state", currentState is LoginUiState.Error)
        val errorState = currentState as LoginUiState.Error
        assertTrue(
            "Should contain network error message",
            errorState.message.contains("Network Error")
        )

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `when socket timeout occurs, should return NetworkError`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_EMAIL.trim(), TEST_PASSWORD)

        // Mock repository throw SocketTimeoutException
        coEvery { loginRepository.login(loginReq) } throws SocketTimeoutException("Connection timeout")

        // When
        viewModel.login(TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Error state", currentState is LoginUiState.Error)
        val errorState = currentState as LoginUiState.Error
        assertTrue(
            "Should contain network error message",
            errorState.message.contains("Network Error")
        )

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `when unknown error occurs, uiState should be Error with unknown error message`() = runTest {
        // Given
        val loginReq = LoginReq(TEST_EMAIL.trim(), TEST_PASSWORD)

        // Mock repository throw unexpected exception
        coEvery { loginRepository.login(loginReq) } throws RuntimeException("Unexpected error")

        // When
        viewModel.login(TEST_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Error state", currentState is LoginUiState.Error)
        val errorState = currentState as LoginUiState.Error
        assertTrue(
            "Should contain error message",
            errorState.message.contains("Login Error")
        )

        coVerify(exactly = 1) { loginRepository.login(loginReq) }
    }

    @Test
    fun `when email is empty, should return InvalidCredentials`() = runTest {
        // Given
        val email = ""

        // UseCase should validate and return InvalidCredentials，not call Repository

        // When
        viewModel.login(email, TEST_PASSWORD)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Error state", currentState is LoginUiState.Error)
        val errorState = currentState as LoginUiState.Error
        assertEquals(
            "Account or Password is not correct",
            errorState.message
        )

        // Repository should not be called for invalid input
        coVerify(exactly = 0) { loginRepository.login(any()) }
    }

    @Test
    fun `when password is empty, should return InvalidCredentials`() = runTest {
        // Given
        val password = ""

        // UseCase should validate and return InvalidCredentials，not call Repository

        // When
        viewModel.login(TEST_EMAIL, password)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Error state", currentState is LoginUiState.Error)
        val errorState = currentState as LoginUiState.Error
        assertEquals(
            "Account or Password is not correct",
            errorState.message
        )

        // Repository should not be called for invalid input
        coVerify(exactly = 0) { loginRepository.login(any()) }
    }

    @Test
    fun `when invalid email format, should return InvalidCredentials`() = runTest {
        // Given: Invalid email format

        // UseCase should validate and return InvalidCredentials，not call Repository

        // When
        viewModel.login(TEST_WRONG_EMAIL, TEST_PASSWORD)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue("Should be Error state", currentState is LoginUiState.Error)
        val errorState = currentState as LoginUiState.Error
        assertEquals(
            "Account or Password is not correct",
            errorState.message
        )

        // Repository should not be called for invalid input
        coVerify(exactly = 0) { loginRepository.login(any()) }
    }
}
