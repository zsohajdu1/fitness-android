package aut.bme.hu.fitness.presentation.creation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import aut.bme.hu.fitness.compose.ErrorDialog
import aut.bme.hu.fitness.domain.model.enums.ActivityLevel
import aut.bme.hu.fitness.domain.model.enums.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationScreen(
    toHome: () -> Unit, viewModel: CreationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is CreationViewModel.CreationUiState.Error -> {
                ErrorDialog(
                    message = state.message, onDismiss = viewModel::refresh
                )
            }

            is CreationViewModel.CreationUiState.Loading -> {
                CircularProgressIndicator()
            }

            is CreationViewModel.CreationUiState.Created -> {
                toHome()
            }

            is CreationViewModel.CreationUiState.Success -> {

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create Your Profile",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.birthDate.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Birthdate") },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.onChangingBirthDateChanged(true)
                            }) {
                                Icon(
                                    Icons.Default.ArrowDropDown, contentDescription = null
                                )
                            }
                        })
                    val datePickerState = rememberDatePickerState()
                    if (state.data.changingBirthDate) {
                        DatePickerDialog(onDismissRequest = {
                            viewModel.onChangingBirthDateChanged(false)
                        }, confirmButton = {
                            TextButton(onClick = {
                                viewModel.onChangingBirthDateChanged(false)
                                viewModel.onBirthDateChanged(datePickerState.selectedDateMillis)
                            }) {
                                Text(text = "OK")
                            }

                        }, dismissButton = {
                            TextButton(onClick = {
                                viewModel.onChangingBirthDateChanged(false)
                            }) {
                                Text(text = "Cancel")
                            }
                        }) {
                            DatePicker(state = datePickerState)
                        }
                    }

                    OutlinedTextField(singleLine = true,
                        value = state.data.gender.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(text = "Gender") },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.onChangingGenderChanged(true)
                            }) {
                                Icon(
                                    Icons.Default.ArrowDropDown, contentDescription = null
                                )
                            }
                        })


                    DropdownMenu(expanded = state.data.changingGender,
                        onDismissRequest = { viewModel.onChangingGenderChanged(false) }) {
                        Text(text = "Gender")
                        Gender.entries.forEach {
                            DropdownMenuItem(onClick = {
                                viewModel.onChangingGenderChanged(false)
                                viewModel.onGenderChanged(it)
                            }, text = { Text(text = it.toString()) })
                        }
                    }
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.height.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            if (it.isDigitsOnly()) viewModel.onHeightChanged(
                                it.toInt()
                            )
                        },
                        suffix = { Text(text = " cm") },
                        label = { Text(text = "Height") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.weight.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            if (it.isDigitsOnly()) viewModel.onWeightChanged(
                                it.toInt()
                            )
                        },
                        suffix = { Text(text = " kg") },
                        label = { Text(text = "Weight") })
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        value = state.data.activityLevel.toString(),
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
                                viewModel.onActivityLevelChanged(it)
                            }, text = { Text(text = it.toString()) })
                        }
                    }
                    Button(onClick = {
                        viewModel.onCreateProfile()
                    }) {
                        Text(text = "Create")
                    }

                }

            }
        }
    }
}
