package aut.bme.hu.fitness.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import aut.bme.hu.fitness.CREATION_SCREEN
import aut.bme.hu.fitness.MAIN_SCREEN

@Composable
fun LoginScreen(
    navigateTo: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    when (val state = uiState) {
        is LoginViewModel.LoginUiState.Loading -> {
            CircularProgressIndicator()
        }

        is LoginViewModel.LoginUiState.Error -> {
            Dialog(onDismissRequest = { }) {
                Text(text = state.message)
            }
        }

        is LoginViewModel.LoginUiState.SuccessWithProfile -> {
            navigateTo(MAIN_SCREEN)
        }

        is LoginViewModel.LoginUiState.SuccessWithoutProfile -> {
            navigateTo(CREATION_SCREEN)
        }

        is LoginViewModel.LoginUiState.Created -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.username,
                        onValueChange = viewModel::onUsernameChanged,
                        label = { Text(text = "Username") }
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.password,
                        onValueChange = viewModel::onPasswordChanged,
                        label = { Text(text = "Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = viewModel::onLoginClick
                    ) {
                        Text("Login")
                    }

                }
            }
        }
    }
}