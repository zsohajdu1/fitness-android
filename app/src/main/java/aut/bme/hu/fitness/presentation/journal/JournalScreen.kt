package aut.bme.hu.fitness.presentation.journal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun JournalScreen(
    viewModel: JournalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.refresh()
    }

    when (val state = uiState) {
        is JournalViewModel.JournalUiState.Error -> {
            Dialog(onDismissRequest = { viewModel.refresh() }) {
                Text(text = state.message)
            }
        }

        is JournalViewModel.JournalUiState.Loading -> {
            CircularProgressIndicator()
        }

        is JournalViewModel.JournalUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)

            ) {
                Row {
                    Button(onClick = { viewModel.onPreviousDay() }) {
                        Text(text = "<")
                    }
                    Text(text = state.data.date.toString())
                    Text(text = state.data.calorieIntakes.sumOf { it.calories }
                        .toString() + "" + "kcal")
                    Button(onClick = { viewModel.onNextDay() }) {
                        Text(text = ">")
                    }
                }
                state.data.calorieIntakes.forEach {
                    Card {
                        Text(text = it.name + " " + it.calories + " " + it.quantity + " " + it.date)
                    }
                }
            }
        }
    }
}