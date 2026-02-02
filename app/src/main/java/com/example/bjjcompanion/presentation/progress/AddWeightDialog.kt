package com.example.bjjcompanion.presentation.progress

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddWeightDialog(
    onDismiss: () -> Unit,
    onConfirm: (Float) -> Unit,
    errorMessage: String? = null
) {
    var weightText by remember { mutableStateOf("") }
    var weightError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Weight Entry") },
        text = {
            Column {
                Text("Enter your current weight in kilograms")

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = weightText,
                    onValueChange = {
                        weightText = it
                        weightError = null
                    },
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = weightError != null || errorMessage != null,
                    supportingText = {
                        (weightError ?: errorMessage)?.let { Text(it) }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val weight = weightText.toFloatOrNull()
                    when {
                        weight == null -> {
                            weightError = "Please enter a valid number"
                        }
                        weight <= 0 -> {
                            weightError = "Weight must be greater than 0"
                        }
                        weight < 20 || weight > 300 -> {
                            weightError = "Weight must be between 20 and 300 kg"
                        }
                        else -> {
                            onConfirm(weight)
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
