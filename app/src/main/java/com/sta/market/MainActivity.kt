package com.sta.market

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sta.market.navigation.Routes
import com.sta.market.presentation.screens.ForgetPasswordScreen
import com.sta.market.presentation.screens.LoginScreen
import com.sta.market.presentation.screens.RegisterScreen
import com.sta.market.ui.theme.StaMarketTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

private const val GREETING_DELAY_MILLIS = 3000L

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StaMarketTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Navigation graph, initial screen is the "greeting screen"
    NavHost(navController = navController, startDestination = Routes.GREETING) {
        composable(Routes.GREETING) {
            GreetingScreen(navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToForgetPassword = {
                    navController.navigate(Routes.FORGET_PASSWORD)
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.FORGET_PASSWORD) {
            ForgetPasswordScreen(navController)
        }

        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }
    }
}

@Composable
fun GreetingScreen(navController: NavHostController) {
    // Delay for 3 seconds before navigating to the login screen
    LaunchedEffect(Unit) {
        delay(GREETING_DELAY_MILLIS)
        navController.navigate(Routes.LOGIN) {
            // Disable back to the greeting screen
            popUpTo(Routes.GREETING) { inclusive = true }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(R.string.app_name))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StaMarketTheme {
        GreetingScreen(rememberNavController())
    }
}
