package com.sta.market.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sta.market.R
import com.sta.market.domain.model.Email
import com.sta.market.domain.model.Password
import com.sta.market.ui.theme.StaMarketTheme

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel()) {
    // State to hold the UI state
    val uiState by viewModel.uiState.collectAsState()
    // State to hold the user's email & secret
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.login_horizontal_padding)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        // Title text: Login
        Text(
            text = stringResource(R.string.login_screen_login_title),
            fontSize = dimensionResource(id = R.dimen.login_title_font_size).value.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.login_title_description_padding)),
            fontWeight = FontWeight.Bold
        )

        // Title Email
        Text(
            text = stringResource(R.string.login_screen_email_title),
            fontSize = dimensionResource(id = R.dimen.login_subtitle_font_size).value.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold
        )

        // TextInput Email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.login_screen_text_input_email_hint)) },
            modifier = Modifier
                .padding(bottom = dimensionResource(id = R.dimen.login_text_input_padding_bottom))
                .fillMaxWidth()
                .testTag("emailInput"),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,    // When the text field is focused
                unfocusedContainerColor = Color.Transparent,  // When the text field is not focused
                disabledContainerColor = Color.Transparent,   // When the text field is disabled
                focusedIndicatorColor = Color.Black,          // When the text field is focused, bottom line color
                unfocusedIndicatorColor = Color.Gray,         // When the text field is not focused, bottom line color
                disabledIndicatorColor = Color.LightGray      // disabled bottom line color
            )
        )

        // Title Password
        Text(
            text = stringResource(R.string.login_screen_password_title),
            fontSize = dimensionResource(id = R.dimen.login_subtitle_font_size).value.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold
        )

        // TextInput Password
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.login_screen_text_input_password_hint)) },
            modifier = Modifier
                .padding(bottom = dimensionResource(id = R.dimen.login_text_input_padding_bottom))
                .fillMaxWidth()
                .testTag("PasswordInput"),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,    // When the text field is focused
                unfocusedContainerColor = Color.Transparent,  // When the text field is not focused
                disabledContainerColor = Color.Transparent,   // When the text field is disabled
                focusedIndicatorColor = Color.Black,          // When the text field is focused, bottom line color
                unfocusedIndicatorColor = Color.Gray,         // When the text field is not focused, bottom line color
                disabledIndicatorColor = Color.LightGray      // disabled bottom line color
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Forget password
            Text(
                text = stringResource(R.string.login_screen_forget_password),
                fontSize = dimensionResource(id = R.dimen.login_subtitle_font_size).value.sp,
                color = Color.Blue,
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.login_text_input_padding_bottom))
                    .clickable {
                        // Handle forget password click
                    }
            )

            // Register
            Text(
                text = stringResource(R.string.login_screen_register),
                fontSize = dimensionResource(id = R.dimen.login_subtitle_font_size).value.sp,
                color = Color.Blue,
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.login_text_input_padding_bottom))
                    .clickable {
                        // Handle Register click
                    }
            )
        }

        // Login error hint
        when (uiState) {
            is LoginUiState.Error -> {
                val message = (uiState as LoginUiState.Error).message
                Text(
                    text = message,
                    color = Color.Red,
                    fontSize = dimensionResource(R.dimen.login_error_hint_size).value.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = dimensionResource(id = R.dimen.login_text_input_padding_bottom))
                )
            }

            else -> {}
        }

        // Login button
        Button(
            onClick = { viewModel.login(Email(email), Password(password)) },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.color_53b175)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("LoginButton")
        ) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.loading_indicator_size))
                )
            } else {
                Text(
                    stringResource(R.string.login_screen_login_title),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    StaMarketTheme {
        LoginScreen()
    }
}
