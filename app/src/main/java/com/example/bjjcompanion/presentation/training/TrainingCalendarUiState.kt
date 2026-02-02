package com.example.bjjcompanion.presentation.training

import com.example.bjjcompanion.domain.model.Training
import java.time.LocalDate

/**
 * UI state for the training calendar screen.
 */
data class TrainingCalendarUiState(
    val currentMonth: LocalDate = LocalDate.now(),
    val trainings: List<Training> = emptyList(),
    val selectedDate: LocalDate? = null,
    val selectedDateTrainings: List<Training> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
