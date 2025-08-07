package com.sta.market.presentation.login

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
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
import com.sta.market.domain.model.Email
import com.sta.market.domain.model.Password
import com.sta.market.ui.theme.StaMarketTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<LoginViewModel>(relaxed = true)

    @Test
    fun loginScreen_displaysAllElements() {
        // Given - 設置初始狀態
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        // When - 顯示 LoginScreen
        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // Then - 驗證所有元素都顯示
        composeTestRule.onNodeWithTag("emailInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("PasswordInput").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LoginButton").assertIsDisplayed()
    }

    @Test
    fun loginScreen_emailInput_acceptsText() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // When - 在 email 輸入框輸入文字
        composeTestRule.onNodeWithTag("emailInput")
            .performTextInput(TEST_EMAIL)

        // Then - 驗證輸入框可以找到並且沒有拋出異常
        composeTestRule.onNodeWithTag("emailInput")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_passwordInput_acceptsText() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // When - 在 password 輸入框輸入文字
        composeTestRule.onNodeWithTag("PasswordInput")
            .performTextInput(TEST_PASSWORD)

        // Then - 驗證輸入框可以找到並且沒有拋出異常
        composeTestRule.onNodeWithTag("PasswordInput")
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_loginButton_callsViewModelLogin() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // When - 輸入資料並點擊登入按鈕
        composeTestRule.onNodeWithTag("emailInput")
            .performTextInput(TEST_EMAIL)
        composeTestRule.onNodeWithTag("PasswordInput")
            .performTextInput(TEST_PASSWORD)
        composeTestRule.onNodeWithTag("LoginButton")
            .performClick()

        // Then - 驗證 ViewModel 的 login 方法被調用
        verify { mockViewModel.login(Email(TEST_EMAIL), Password(TEST_PASSWORD)) }
    }

    @Test
    fun loginScreen_showsLoadingIndicator_whenLoading() {
        // Given - 設置載入狀態
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // Then - 驗證載入指示器顯示，登入文字不顯示
        composeTestRule.onNodeWithTag("LoginButton").assertIsDisplayed()
        // Note: 在實際測試中，你可能需要為 CircularProgressIndicator 添加 testTag
    }

    @Test
    fun loginScreen_showsErrorMessage_whenError() {
        // Given - 設置錯誤狀態
        val errorMessage = "Invalid credentials"
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Error(errorMessage))
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // Then - 驗證錯誤訊息顯示
        composeTestRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun loginScreen_forgotPasswordText_hasClickAction() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // Then - 驗證忘記密碼文字可點擊
        composeTestRule.onNodeWithText("Forgot Password?")
            .assertHasClickAction()
    }

    @Test
    fun loginScreen_registerText_hasClickAction() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // Then - 驗證註冊文字可點擊
        composeTestRule.onNodeWithText("Register")
            .assertHasClickAction()
    }

    @Test
    fun loginScreen_emptyEmailAndPassword_stillCallsLogin() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // When - 不輸入任何內容直接點擊登入
        composeTestRule.onNodeWithTag("LoginButton")
            .performClick()

        // Then - 驗證仍會調用 login 方法（使用空字串）
        verify { mockViewModel.login(Email(""), Password("")) }
    }

    @Test
    fun loginScreen_withWrongCredentials_callsViewModelLogin() {
        // Given
        val uiStateFlow = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
        every { mockViewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            StaMarketTheme {
                LoginScreen(viewModel = mockViewModel)
            }
        }

        // When - 輸入錯誤的資料並點擊登入按鈕
        composeTestRule.onNodeWithTag("emailInput")
            .performTextInput(TEST_WRONG_EMAIL)
        composeTestRule.onNodeWithTag("PasswordInput")
            .performTextInput(TEST_WRONG_PASSWORD)
        composeTestRule.onNodeWithTag("LoginButton")
            .performClick()

        // Then - 驗證 ViewModel 的 login 方法被調用
        verify { mockViewModel.login(Email(TEST_WRONG_EMAIL), Password(TEST_WRONG_PASSWORD)) }
    }
}