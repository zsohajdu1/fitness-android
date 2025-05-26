package aut.bme.hu.fitness.presentation.journal

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import aut.bme.hu.fitness.compose.ErrorDialog
import aut.bme.hu.fitness.compose.IntakeCard
import aut.bme.hu.fitness.compose.ExerciseCard

@Composable
fun JournalScreen(
    viewModel: JournalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        when (val state = uiState) {
            is JournalViewModel.JournalUiState.Error -> {
                ErrorDialog(
                    message = state.message,
                    onDismiss = viewModel::refresh
                )
            }

            is JournalViewModel.JournalUiState.Loading -> {
                CircularProgressIndicator()
            }

            is JournalViewModel.JournalUiState.Success -> {
                val calorieIntakes = state.data.calorieIntakes
                val exercises = state.data.exercises

                val totalIntakeCalories = calorieIntakes.sumOf { (it.calories * it.quantity).toInt() }
                val totalExerciseCalories = exercises.sumOf { it.calories.toInt() }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    DateNavigationRow(
                        date = state.data.date.toString(),
                        totalIntakeCalories = totalIntakeCalories,
                        totalExerciseCalories = totalExerciseCalories,
                        steps = state.data.steps,
                        onPreviousDay = viewModel::onPreviousDay,
                        onNextDay = viewModel::onNextDay
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Calorie Intakes",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            calorieIntakes.forEach { intake ->
                                IntakeCard(calorieIntake = intake)
                            }
                        }

                        if (exercises.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Exercises",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                exercises.forEach { exercise ->
                                    ExerciseCard(exercise = exercise)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DateNavigationRow(
    date: String,
    totalIntakeCalories: Int,
    totalExerciseCalories: Int,
    steps: Int,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onPreviousDay) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Day"
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Intake: $totalIntakeCalories kcal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Exercise: $totalExerciseCalories kcal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Steps: $steps",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onNextDay) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Day"
                    )
                }
            }
        }
    }
}
