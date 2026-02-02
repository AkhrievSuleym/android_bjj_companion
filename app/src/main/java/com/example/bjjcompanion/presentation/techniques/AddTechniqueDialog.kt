package com.example.bjjcompanion.presentation.techniques

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.domain.model.Technique
import com.example.bjjcompanion.domain.model.TechniqueCategory
import com.example.bjjcompanion.domain.usecase.technique.AddTechniqueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Dialog for adding a new technique.
 */
@Composable
fun AddTechniqueDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: AddTechniqueDialogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle save success
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onSuccess()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Add Technique",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Name input
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = viewModel::updateName,
                    label = { Text("Technique Name") },
                    isError = uiState.nameError != null,
                    supportingText = uiState.nameError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category selector
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                TechniqueCategory.entries.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = uiState.category == category,
                            onClick = { viewModel.updateCategory(category) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = category.name.replace("_", " ").lowercase()
                                .replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(androidx.compose.ui.Alignment.CenterVertically)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description input
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = viewModel::updateDescription,
                    label = { Text("Notes (optional)") },
                    isError = uiState.descriptionError != null,
                    supportingText = uiState.descriptionError?.let { { Text(it) } },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSaving
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = viewModel::saveTechnique,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Save")
                        }
                    }
                }

                // Error message
                uiState.saveError?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// ViewModel for the dialog
@HiltViewModel
class AddTechniqueDialogViewModel @Inject constructor(
    private val addTechniqueUseCase: AddTechniqueUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTechniqueUiState())
    val uiState: StateFlow<AddTechniqueUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        val nameError = if (name.isBlank()) "Name is required" else null
        _uiState.update { it.copy(name = name, nameError = nameError) }
    }

    fun updateCategory(category: TechniqueCategory) {
        _uiState.update { it.copy(category = category) }
    }

    fun updateDescription(description: String) {
        val descriptionError = if (description.length > 2000) {
            "Description must be less than 2000 characters"
        } else null

        _uiState.update {
            it.copy(description = description, descriptionError = descriptionError)
        }
    }

    fun saveTechnique() {
        val state = _uiState.value

        // Validate
        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Name is required") }
            return
        }

        if (state.description.length > 2000) {
            _uiState.update { it.copy(descriptionError = "Description too long") }
            return
        }

        // Create technique
        val technique = Technique(
            id = 0,
            name = state.name,
            category = state.category,
            description = state.description.ifBlank { null },
            createdAt = System.currentTimeMillis()
        )

        // Save
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveError = null) }

            addTechniqueUseCase(technique)
                .onSuccess { _ ->
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            saveError = error.message ?: "Failed to save technique"
                        )
                    }
                }
        }
    }
}

data class AddTechniqueUiState(
    val name: String = "",
    val category: TechniqueCategory = TechniqueCategory.SUBMISSION,
    val description: String = "",
    val nameError: String? = null,
    val descriptionError: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val saveError: String? = null
)
