package aut.bme.hu.fitness.presentation.register

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

@Composable
fun RegisterScreen(
    navigateTo: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    when (val state = uiState) {
        is RegisterViewModel.RegisterUiState.Loading -> {
            CircularProgressIndicator()
        }

        is RegisterViewModel.RegisterUiState.Error -> {
            Dialog(onDismissRequest = { }) {
                Text(text = state.message)
            }
        }

        is RegisterViewModel.RegisterUiState.Success -> {
            navigateTo()
        }

        is RegisterViewModel.RegisterUiState.Created -> {
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
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.repeatPassword,
                        onValueChange = viewModel::onRepeatPasswordChanged,
                        label = { Text(text = "Repeat password") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = viewModel::onRegisterClick
                    ) {
                        Text("Register")
                    }

                }
            }
        }
    }
}