package aut.bme.hu.fitness.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import aut.bme.hu.fitness.domain.model.Exercise


@Composable
fun ExerciseCard(exercise: Exercise) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Name: ${exercise.name}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Calories burned: ${exercise.calories.toInt()}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
