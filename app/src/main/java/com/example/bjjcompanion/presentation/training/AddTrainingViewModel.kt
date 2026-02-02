package com.example.bjjcompanion.presentation.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.domain.model.Training
import com.example.bjjcompanion.domain.model.TrainingType
import com.example.bjjcompanion.domain.usecase.training.AddTrainingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * ViewModel for adding/editing training sessions.
 */
@HiltViewModel
class AddTrainingViewModel @Inject constructor(
    private val addTrainingUseCase: AddTrainingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTrainingUiState())
    val uiState: StateFlow<AddTrainingUiState> = _uiState.asStateFlow()

    /**
     * Update the training date.
     */
    fun updateDate(date: LocalDate) {
        _uiState.update { it.copy(date = date) }
    }

    /**
     * Update the training type.
     */
    fun updateType(type: TrainingType) {
        _uiState.update { it.copy(type = type) }
    }

    /**
     * Update the duration in minutes.
     */
    fun updateDuration(duration: String) {
        val durationError = when {
            duration.isBlank() -> "Duration is required"
            duration.toIntOrNull() == null -> "Duration must be a number"
            duration.toInt() <= 0 -> "Duration must be positive"
            else -> null
        }

        _uiState.update {
            it.copy(
                durationMinutes = duration,
                durationError = durationError
            )
        }
    }

    /**
     * Update the training notes.
     */
    fun updateNotes(notes: String) {
        val notesError = if (notes.length > 1000) {
            "Notes must be less than 1000 characters"
        } else null

        _uiState.update {
            it.copy(
                notes = notes,
                notesError = notesError
            )
        }
    }

    /**
     * Save the training session.
     */
    fun saveTraining() {
        val state = _uiState.value

        // Validate form
        if (state.durationMinutes.isBlank()) {
            _uiState.update { it.copy(durationError = "Duration is required") }
            return
        }

        val duration = state.durationMinutes.toIntOrNull()
        if (duration == null || duration <= 0) {
            _uiState.update { it.copy(durationError = "Duration must be a positive number") }
            return
        }

        if (state.notes.length > 1000) {
            _uiState.update { it.copy(notesError = "Notes must be less than 1000 characters") }
            return
        }

        // Create training
        val timestamp = state.date
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val training = Training(
            id = 0,
            date = timestamp,
            type = state.type,
            durationMinutes = duration,
            notes = state.notes.ifBlank { null }
        )

        // Save training
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveError = null) }

            addTrainingUseCase(training)
                .onSuccess { _ ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            saveSuccess = true
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            saveError = error.message ?: "Failed to save training"
                        )
                    }
                }
        }
    }

    /**
     * Reset the form after successful save.
     */
    fun resetForm() {
        _uiState.value = AddTrainingUiState()
    }
}
