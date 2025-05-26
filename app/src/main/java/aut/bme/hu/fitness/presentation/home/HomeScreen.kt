package aut.bme.hu.fitness.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import aut.bme.hu.fitness.compose.ErrorDialog
import aut.bme.hu.fitness.compose.ExerciseCard
import aut.bme.hu.fitness.compose.InfoCard
import aut.bme.hu.fitness.compose.InfoRow
import aut.bme.hu.fitness.compose.IntakeCard
import aut.bme.hu.fitness.presentation.steps.StepCounterObserver


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
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is HomeViewModel.HomeUiState.Loading -> {
                CircularProgressIndicator()
            }

            is HomeViewModel.HomeUiState.Error -> {
                ErrorDialog(
                    message = state.message, onDismiss = viewModel::refresh
                )
            }

            is HomeViewModel.HomeUiState.Success -> {
                RequestActivityRecognitionPermission()
                StepCounterObserver()
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
                        title = "Overview", content = {
                            Column {
                                InfoRow(
                                    label = "Total Daily Energy Expenditure",
                                    value = state.data.userProfile.tdee?.toInt().toString()
                                )
                                InfoRow(
                                    label = "Total Calories",
                                    value = state.data.calorieIntakes.sumOf { it.calories.toInt() }
                                        .toString())
                                if (state.data.userProfile.manualExercise) {
                                    InfoRow(
                                        label = "Total Burned",
                                        value = state.data.exercises.sumOf { it.calories.toInt() }
                                            .toString())
                                }
                                InfoRow(label = "Steps", value = state.data.steps.count.toString())
                            }
                        })
                    InfoCard(title = "Calorie Intakes", trailingIcon = {
                        IconButton(onClick = { viewModel.onCreatingCalorieIntakeChanged(true) }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Calorie Intake")
                        }
                    }, content = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            state.data.calorieIntakes.forEach { intake ->
                                IntakeCard(calorieIntake = intake)
                            }
                        }
                    })
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
                                if (it.isDigitsOnly()) viewModel.onCreatingCalorieIntakeCaloriesChanged(
                                    it.toDouble()
                                )
                            },
                            intakeQuantity = state.data.creatingCalorieIntakeQuantity,
                            onQuantityChange = {
                                if (it.isDigitsOnly()) viewModel.onCreatingCalorieIntakeQuantityChanged(
                                    it.toInt()
                                )
                            })
                    }

                    if (state.data.userProfile.manualExercise) {
                        InfoCard(title = "Exercises", trailingIcon = {
                            IconButton(onClick = { viewModel.onCreatingExerciseChanged(true) }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Exercise")
                            }
                        }, content = {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                state.data.exercises.forEach { exercise ->
                                    ExerciseCard(exercise = exercise)
                                }
                            }
                        })
                    }

                    if (state.data.creatingExercise) {
                        CreateExerciseDialog(
                            onDismiss = { viewModel.onCreatingExerciseChanged(false) },
                            onCreate = {
                                viewModel.onCreatingExerciseChanged(false)
                                viewModel.onCreatingExercise()
                            },
                            exerciseName = state.data.creatingExerciseName,
                            onNameChange = viewModel::onCreatingExerciseNameChanged,
                            exerciseCalories = state.data.creatingExerciseCalories.toInt(),
                            onCaloriesChange = {
                                if (it.isDigitsOnly()) viewModel.onCreatingExerciseCaloriesChanged(
                                    it.toDouble()
                                )
                            })
                    }

                }
            }
        }
    }
}

@Composable
fun RequestActivityRecognitionPermission() {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {  }
    )

    LaunchedEffect(true) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }
}

