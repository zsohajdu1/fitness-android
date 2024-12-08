package aut.bme.hu.fitness.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(60.dp),
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
                        .padding(4.dp)

                ) {
                    /*
                    state.data.userProfile.tdee?.let {
                        createPanel(
                            it,
                            state.data.calorieIntakes.sumOf { it.calories })
                    }?.let {
                        PlotPanel(
                            figure = it,
                            preserveAspectRatio = true,
                            modifier = Modifier.fillMaxSize()
                        ) {}
                    }*/
                    Card {
                        Column(
                            modifier = Modifier
                                .padding(4.dp)

                        ) {
                            Text(text = "Total Daily Energy Expenditure: " + state.data.userProfile.tdee.toString())
                            Text(text = "Total Calories: " + state.data.calorieIntakes.sumOf { it.calories }.toString())
                        }
                    }
                    Card {
                        IconButton(
                            onClick = { viewModel.onCreatingCalorieIntakeChanged(true) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add calorie intake"
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )
                        state.data.calorieIntakes.forEach {
                            Card {
                                Text(text = it.name + " " + it.calories + " " + it.quantity + " " + it.calories * it.quantity)
                            }
                        }
                    }
                    when {
                        state.data.creatingCalorieIntake -> {
                            Dialog(onDismissRequest = {
                                viewModel.onCreatingCalorieIntakeChanged(
                                    false
                                )
                            }) {
                                Card {
                                    Column (
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(8.dp)

                                    ) {
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
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
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
}

/*fun createPanel(tdee: Double, calories: Double): Figure {
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
}*/