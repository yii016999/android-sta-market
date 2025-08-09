package com.sta.market.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sta.market.domain.result.LoginResult
import com.sta.market.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {

        if (!isValidEmail(email)) {
            _uiState.value = LoginUiState.Error("Account or Password is not correct")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = loginUseCase(email, password)

            _uiState.value = when (result) {
                is LoginResult.Success -> LoginUiState.Success
                is LoginResult.InvalidCredentials -> LoginUiState.Error("Account or Password is not correct")
                is LoginResult.AccountLocked -> LoginUiState.Error("Your account has been locked, Please contact the administrator")
                is LoginResult.NetworkError -> LoginUiState.Error("Network Error：${result.message}")
                is LoginResult.UnknownError -> LoginUiState.Error("Login Error：${result.message}")
            }
        }
    }

    private fun isValidEmail(raw: String): Boolean {
        val email = raw.trim()
        if (email.isEmpty()) return false
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return regex.matches(email)
    }
}
