package com.example.bjjcompanion.presentation.training

import com.example.bjjcompanion.domain.model.TrainingType
import java.time.LocalDate

/**
 * UI state for adding/editing training session.
 */
data class AddTrainingUiState(
    val date: LocalDate = LocalDate.now(),
    val type: TrainingType = TrainingType.GRAPPLING,
    val durationMinutes: String = "",
    val notes: String = "",
    val durationError: String? = null,
    val notesError: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val saveError: String? = null
)
