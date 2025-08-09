package com.sta.market.domain.usecase

import android.util.Log
import com.sta.market.domain.model.AccountLockedException
import com.sta.market.domain.model.LoginReq
import com.sta.market.domain.model.UnauthorizedException
import com.sta.market.domain.repository.LoginRepository
import com.sta.market.domain.result.LoginResult
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        return try {
            val trimmedMail = email.trim()
            if (trimmedMail.isBlank() || password.isBlank()) {
                return LoginResult.InvalidCredentials
            }

            // Build the request here
            val request = LoginReq(email = trimmedMail.trim(), password = password)

            // Call Repository
            val response = repository.login(request)

            // Return the result (Success)
            LoginResult.Success(token = response.token)

        } catch (e: AccountLockedException) {
            Log.e("LoginUseCase", "AccountLockedException: ${e.message}")
            LoginResult.AccountLocked
        } catch (e: UnauthorizedException) {
            Log.e("LoginUseCase", "UnauthorizedException: ${e.message}")
            LoginResult.InvalidCredentials
        } catch (e: UnauthorizedException) {
            Log.e("LoginUseCase", "UnauthorizedException: ${e.message}")
            LoginResult.InvalidCredentials
        } catch (e: IOException) {
            LoginResult.NetworkError("Network error: ${e.message}")
        } catch (e: Exception) {
            LoginResult.UnknownError(e.message ?: "Unknown error occurred")
        }
    }
}

