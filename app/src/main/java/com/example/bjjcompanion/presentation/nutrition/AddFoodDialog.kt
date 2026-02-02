package com.example.bjjcompanion.presentation.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bjjcompanion.domain.model.Food

@Composable
fun AddFoodDialog(
    food: Food,
    onDismiss: () -> Unit,
    onConfirm: (Float) -> Unit,
    errorMessage: String? = null
) {
    var gramsText by remember { mutableStateOf("100") }
    var gramsError by remember { mutableStateOf<String?>(null) }

    // Calculate nutrition for current grams
    val grams = gramsText.toFloatOrNull() ?: 0f
    val multiplier = grams / 100f

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add ${food.name}") },
        text = {
            Column {
                Text(
                    text = "Enter portion size",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = gramsText,
                    onValueChange = {
                        gramsText = it
                        gramsError = null
                    },
                    label = { Text("Grams") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = gramsError != null || errorMessage != null,
                    supportingText = {
                        (gramsError ?: errorMessage)?.let { Text(it) }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nutrition Preview
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Nutrition for ${grams.toInt()}g",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            NutrientColumn(
                                label = "Calories",
                                value = "${(food.caloriesPer100g * multiplier).toInt()} kcal"
                            )
                            NutrientColumn(
                                label = "Protein",
                                value = "${(food.proteinPer100g * multiplier).toInt()}g"
                            )
                            NutrientColumn(
                                label = "Fat",
                                value = "${(food.fatPer100g * multiplier).toInt()}g"
                            )
                            NutrientColumn(
                                label = "Carbs",
                                value = "${(food.carbsPer100g * multiplier).toInt()}g"
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val gramsValue = gramsText.toFloatOrNull()
                    when {
                        gramsValue == null -> {
                            gramsError = "Please enter a valid number"
                        }
                        gramsValue <= 0 -> {
                            gramsError = "Grams must be greater than 0"
                        }
                        gramsValue > 10000 -> {
                            gramsError = "Grams cannot exceed 10kg"
                        }
                        else -> {
                            onConfirm(gramsValue)
                        }
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun NutrientColumn(
    label: String,
    value: String
) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
