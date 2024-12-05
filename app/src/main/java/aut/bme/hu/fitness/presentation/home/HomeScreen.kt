package aut.bme.hu.fitness.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.Stat
import org.jetbrains.letsPlot.geom.geomPie
import org.jetbrains.letsPlot.letsPlot
import org.jetbrains.letsPlot.skia.compose.PlotPanel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    when (val state = uiState) {
        is HomeViewModel.HomeUiState.Loading -> {
            CircularProgressIndicator()
        }

        is HomeViewModel.HomeUiState.Error -> {
            Dialog(onDismissRequest = { viewModel.refresh() }) {
                Text(text = state.message)
            }
        }

        is HomeViewModel.HomeUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)

            ) {
                PlotPanel(
                    figure = createPanel(
                        state.data.userProfile.tdee,
                        state.data.calorieIntakes.sumOf { it.calories }),
                    preserveAspectRatio = true,
                    modifier = Modifier.fillMaxSize()
                ) {}
                Card {
                    Button(
                        onClick = { viewModel.onCreatingCalorieIntakeChanged(true) },
                        modifier = Modifier.padding(16.dp)
                    ) {}
                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )
                    state.data.calorieIntakes.forEach {
                        Card {
                            Text(text = it.name + " " + it.calories + " " + it.quantity + " " + it.date)
                        }
                    }
                }
                when {
                    state.data.creatingCalorieIntake -> {
                        Dialog(onDismissRequest = { viewModel.onCreatingCalorieIntakeChanged(false) }) {
                            Card {
                                Column {
                                    Text(text = "Create calorie intake")
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        value = state.data.creatingCalorieIntakeName,
                                        onValueChange = viewModel::onCreatingCalorieIntakeNameChanged,
                                        label = { Text(text = "Name") }
                                    )
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        value = state.data.creatingCalorieIntakeCalories.toString(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = {
                                            if (it.isDigitsOnly()) viewModel.onCreatingCalorieIntakeCaloriesChanged(
                                                it.toDouble()
                                            )
                                        },
                                        label = { Text(text = "Calories") }
                                    )
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                        value = state.data.creatingCalorieIntakeQuantity.toString(),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = {
                                            if (it.isDigitsOnly()) viewModel.onCreatingCalorieIntakeQuantityChanged(
                                                it.toInt()
                                            )
                                        },
                                        label = { Text(text = "Quantity") }
                                    )
                                    Row {
                                        Button(onClick = {
                                            viewModel.onCreatingCalorieIntakeChanged(false)
                                            viewModel.onCreatingCalorieIntake()
                                        }) {
                                            Text(text = "Create")
                                        }
                                        Button(onClick = {
                                            viewModel.onCreatingCalorieIntakeChanged(
                                                false
                                            )
                                        }) {
                                            Text(text = "Cancel")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun createPanel(tdee: Double, calories: Double): Figure {
    val data = mapOf(
        "category" to listOf("Taken", "Remaing"),
        "value" to listOf(calories, if (calories > tdee) 0 else tdee - calories)
    )
    return letsPlot(data) + geomPie(
        stat = Stat.identity,
        alpha = 0.8
    ) {
        x = "category"
        fill = "category"
        weight = "value"
    }
}