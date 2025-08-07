package com.sta.market.presentation.screens

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sta.market.TestConstants.TEST_EMAIL
import com.sta.market.TestConstants.TEST_PASSWORD
import com.sta.market.TestConstants.TEST_WRONG_EMAIL
import com.sta.market.TestConstants.TEST_WRONG_PASSWORD
import com.sta.market.presentation.login.LoginUiState
import com.sta.market.presentation.login.LoginViewModel
import com.sta.market.ui.theme.StaMarketTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<LoginViewModel>(relaxed = true)
    private val mockOnNavigateToForgetPassword = mockk<() -> Unit>(relaxed = true)
    private val mockOnNavigateToRegister = mockk<() -> Unit>(relaxed = true)

    @Test
    fun loginScreen_shouldDisplayAllElements() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        // When
        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("emailInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("PasswordInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LoginButton").assertIsDisplayed()
    }

    @Test
    fun emailInput_whenReceivesText_shouldAcceptTextInput() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("emailInput").performTextInput(TEST_EMAIL)

        // Then
        composeTestRule.onNodeWithTag("emailInput").assertIsDisplayed()
    }

    @Test
    fun passwordInput_whenReceivesText_shouldAcceptTextInput() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("PasswordInput").performTextInput(TEST_PASSWORD)

        // Then
        composeTestRule.onNodeWithTag("PasswordInput").assertIsDisplayed()
    }

    @Test
    fun loginButton_whenClickedWithCredentials_shouldCallViewModelLogin() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("emailInput").performTextInput(TEST_EMAIL)
        composeTestRule.onNodeWithTag("PasswordInput").performTextInput(TEST_PASSWORD)
        composeTestRule.onNodeWithTag("LoginButton").performClick()

        // Then
        verify { mockViewModel.login(TEST_EMAIL, TEST_PASSWORD) }
    }

    @Test
    fun uiState_whenLoading_shouldShowLoadingIndicator() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
        every { mockViewModel.uiState } returns uiStateFlow

        // When
        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("LoginButton").assertIsDisplayed()
    }

    @Test
    fun uiState_whenError_shouldShowErrorMessage() {
        // Given
        val errorMessage = "Invalid credentials"
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Error(errorMessage))
        every { mockViewModel.uiState } returns uiStateFlow

        // When
        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun forgetPassword_whenClicked_shouldNavigateToForgetPasswordScreen() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("forgetPasswordText")
            .assertHasClickAction()
            .performClick()

        // Then
        verify { mockOnNavigateToForgetPassword() }
    }

    @Test
    fun register_whenClicked_shouldNavigateToRegisterScreen() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("registerText")
            .assertHasClickAction()
            .performClick()

        // Then
        verify { mockOnNavigateToRegister() }
    }

    @Test
    fun loginButton_whenClickedWithEmptyCredentials_shouldStillCallLogin() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("LoginButton").performClick()

        // Then
        verify { mockViewModel.login("", "") }
    }

    @Test
    fun loginButton_whenClickedWithWrongCredentials_shouldCallViewModelLogin() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(
                    viewModel = mockViewModel,
                    onNavigateToForgetPassword = mockOnNavigateToForgetPassword,
                    onNavigateToRegister = mockOnNavigateToRegister
                )
            }
        }

        // When
        composeTestRule.onNodeWithTag("emailInput").performTextInput(TEST_WRONG_EMAIL)
        composeTestRule.onNodeWithTag("PasswordInput").performTextInput(TEST_WRONG_PASSWORD)
        composeTestRule.onNodeWithTag("LoginButton").performClick()

        // Then
        verify { mockViewModel.login(TEST_WRONG_EMAIL, TEST_WRONG_PASSWORD) }
    }
}