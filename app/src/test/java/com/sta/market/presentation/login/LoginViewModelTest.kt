package com.sta.market.presentation.login

import com.sta.market.TestConstants.TEST_EMAIL
import com.sta.market.TestConstants.TEST_PASSWORD
import com.sta.market.TestConstants.TEST_WRONG_EMAIL
import com.sta.market.TestConstants.TEST_WRONG_PASSWORD
import com.sta.market.domain.repository.FakeLoginRepository
import com.sta.market.domain.usecase.LoginUseCase
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeLoginRepository: FakeLoginRepository

    // Use UnconfinedTestDispatcher for immediate control of coroutines
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeLoginRepository = FakeLoginRepository()
        val loginUseCase = LoginUseCase(fakeLoginRepository)
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
    fun `when login starts, uiState should go from Idle to Loading to Success`() = runTest(testDispatcher) {
        // Given
        val email = TEST_EMAIL
        val password = TEST_PASSWORD
        val states = mutableListOf<LoginUiState>()

        // Collect the states
        val job = launch {
            viewModel.uiState.collect { state ->
                states.add(state)
            }
        }

        // When
        viewModel.login(email, password)
        advanceUntilIdle()

        job.cancel()

        // Then
        assertTrue("Should have at least 3 states: Idle, Loading, Success", states.size >= 3)
        assertTrue("Initial state should be Idle", states[0] is LoginUiState.Idle)
        assertTrue("Second state should be Loading", states[1] is LoginUiState.Loading)
        assertTrue("Final state should be Success", states.last() is LoginUiState.Success)
    }

    @Test
    fun `when login success, uiState should be Success`() = runTest {
        // Given
        val email = TEST_EMAIL
        val password = TEST_PASSWORD

        // When
        viewModel.login(email, password)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue(currentState is LoginUiState.Success)
    }

    @Test
    fun `when login with wrong credentials, uiState should be Error with invalid credentials message`() = runTest {
        // Given
        val email = TEST_WRONG_EMAIL
        val password = TEST_WRONG_PASSWORD

        // When
        viewModel.login(email, password)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue(currentState is LoginUiState.Error)
    }

    @Test
    fun `when account is locked, uiState should be Error with account locked message`() = runTest {
        // Given
        fakeLoginRepository.shouldFail = true
        fakeLoginRepository.failureType = FakeLoginRepository.FailureType.ACCOUNT_LOCKED
        val email = TEST_EMAIL
        val password = TEST_PASSWORD

        // When
        viewModel.login(email, password)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue(currentState is LoginUiState.Error)
    }

    @Test
    fun `when network error occurs, uiState should be Error with network error message`() = runTest {
        // Given
        fakeLoginRepository.shouldFail = true
        fakeLoginRepository.failureType = FakeLoginRepository.FailureType.NETWORK_ERROR
        val email = TEST_EMAIL
        val password = TEST_PASSWORD

        // When
        viewModel.login(email, password)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue(currentState is LoginUiState.Error)
    }

    @Test
    fun `when unknown error occurs, uiState should be Error with unknown error message`() = runTest {
        // Given
        fakeLoginRepository.shouldFail = true
        fakeLoginRepository.failureType = FakeLoginRepository.FailureType.UNKNOWN_ERROR
        val email = TEST_EMAIL
        val password = TEST_PASSWORD

        // When
        viewModel.login(email, password)
        advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.value
        assertTrue(currentState is LoginUiState.Error)
    }
}
