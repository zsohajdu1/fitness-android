package aut.bme.hu.fitness.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CreateExerciseDialog(
    onDismiss: () -> Unit,
    onCreate: () -> Unit,
    exerciseName: String,
    onNameChange: (String) -> Unit,
    exerciseCalories: Int,
    onCaloriesChange: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Create Exercise", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = exerciseName,
                    onValueChange = onNameChange,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = exerciseCalories.toString(),
                    onValueChange = onCaloriesChange,
                    label = { Text("Calories") },
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