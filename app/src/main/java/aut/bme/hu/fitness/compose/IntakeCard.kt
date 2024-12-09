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
import aut.bme.hu.fitness.domain.model.CalorieIntake


@Composable
fun IntakeCard(calorieIntake: CalorieIntake) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Name: ${calorieIntake.name}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Calories: ${calorieIntake.calories.toInt()}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Quantity: ${calorieIntake.quantity}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Total: ${(calorieIntake.calories * calorieIntake.quantity).toInt()}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}