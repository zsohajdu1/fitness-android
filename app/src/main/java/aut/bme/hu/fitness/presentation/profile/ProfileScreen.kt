package aut.bme.hu.fitness.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import aut.bme.hu.fitness.compose.ErrorDialog
import aut.bme.hu.fitness.domain.model.enums.ActivityLevel

@Composable
fun ProfileScreen(
    logOut: () -> Unit, viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(60.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is ProfileViewModel.ProfileUiState.Error -> {
                ErrorDialog(
                    message = state.message, onDismiss = viewModel::refresh
                )
            }

            is ProfileViewModel.ProfileUiState.Loading -> {
                CircularProgressIndicator()
            }

            is ProfileViewModel.ProfileUiState.Success -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Your profile")
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.userProfile.birthDate.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Birthdate") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.userProfile.gender.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Gender") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.newHeight.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            if (it.isDigitsOnly()) viewModel.onNewHeightChanged(
                                it.toInt()
                            )
                        },
                        suffix = { Text(text = " cm") },
                        label = { Text(text = "Height") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.newWeight.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            if (it.isDigitsOnly()) viewModel.onNewWeightChanged(
                                it.toInt()
                            )
                        },
                        suffix = { Text(text = " kg") },
                        label = { Text(text = "Weight") })

                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.userProfile.activityLevel.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Activity level") },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.onChangingActivityLevelChanged(true)
                            }) {
                                Icon(
                                    Icons.Default.ArrowDropDown, contentDescription = null
                                )
                            }
                        })

                    DropdownMenu(expanded = state.data.changingActivityLevel,
                        onDismissRequest = { viewModel.onChangingActivityLevelChanged(false) }) {
                        ActivityLevel.entries.forEach {
                            DropdownMenuItem(onClick = {
                                viewModel.onChangingActivityLevelChanged(false)
                                viewModel.onNewActivityLevelChanged(it)
                            }, text = { Text(text = it.toString()) })
                        }
                    }

                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.userProfile.tdee.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Total Daily Energy Expenditure") })
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = {
                            viewModel.onUpdateProfile()
                        }) {
                            Text(text = "Update")
                        }
                        Button(onClick = {
                            viewModel.onLogOut()
                            logOut()
                        }) {
                            Text(text = "Log out")
                        }
                    }
                }
            }
        }
    }
}
