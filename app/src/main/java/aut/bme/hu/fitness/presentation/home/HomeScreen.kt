package aut.bme.hu.fitness.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import aut.bme.hu.fitness.compose.InfoCard
import aut.bme.hu.fitness.compose.InfoRow
import aut.bme.hu.fitness.compose.IntakeCard


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is HomeViewModel.HomeUiState.Loading -> {
                CircularProgressIndicator()
            }

            is HomeViewModel.HomeUiState.Error -> {
                ErrorDialog(
                    message = state.message,
                    onDismiss = viewModel::refresh
                )
            }

            is HomeViewModel.HomeUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(
                        modifier = Modifier.height(30.dp)
                    )
                    InfoCard(
                        title = "Overview",
                        content = {
                            Column {
                                InfoRow(label = "Total Daily Energy Expenditure", value = state.data.userProfile.tdee?.toInt().toString())
                                InfoRow(label = "Total Calories", value = state.data.calorieIntakes.sumOf { it.calories.toInt() }.toString())
                            }
                        }
                    )
                    InfoCard(
                        title = "Calorie Intakes",
                        trailingIcon = {
                            IconButton(onClick = { viewModel.onCreatingCalorieIntakeChanged(true) }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Calorie Intake")
                            }
                        },
                        content = {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                state.data.calorieIntakes.forEach { intake ->
                                    IntakeCard(calorieIntake = intake)
                                }
                            }
                        }
                    )
                    if (state.data.creatingCalorieIntake) {
                        CreateCalorieIntakeDialog(
                            onDismiss = { viewModel.onCreatingCalorieIntakeChanged(false) },
                            onCreate = {
                                viewModel.onCreatingCalorieIntakeChanged(false)
                                viewModel.onCreatingCalorieIntake()
                            },
                            intakeName = state.data.creatingCalorieIntakeName,
                            onNameChange = viewModel::onCreatingCalorieIntakeNameChanged,
                            intakeCalories = state.data.creatingCalorieIntakeCalories.toInt(),
                            onCaloriesChange = {
                                if (it.isDigitsOnly()) viewModel.onCreatingCalorieIntakeCaloriesChanged(it.toDouble())
                            },
                            intakeQuantity = state.data.creatingCalorieIntakeQuantity,
                            onQuantityChange = {
                                if (it.isDigitsOnly()) viewModel.onCreatingCalorieIntakeQuantityChanged(it.toInt())
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CreateCalorieIntakeDialog(
    onDismiss: () -> Unit,
    onCreate: () -> Unit,
    intakeName: String,
    onNameChange: (String) -> Unit,
    intakeCalories: Int,
    onCaloriesChange: (String) -> Unit,
    intakeQuantity: Int,
    onQuantityChange: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Create Calorie Intake", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = intakeName,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = intakeCalories.toString(),
                    onValueChange = onCaloriesChange,
                    label = { Text("Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = intakeQuantity.toString(),
                    onValueChange = onQuantityChange,
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onCreate, modifier = Modifier.weight(1f)) {
                        Text("Create")
                    }
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
