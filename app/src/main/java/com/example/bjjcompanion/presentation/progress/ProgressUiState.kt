package com.example.bjjcompanion.presentation.progress

import com.example.bjjcompanion.domain.model.WeightEntry
import com.example.bjjcompanion.domain.usecase.weight.TimePeriod

data class ProgressUiState(
    val weightHistory: List<WeightEntry> = emptyList(),
    val selectedPeriod: TimePeriod = TimePeriod.MONTH,
    val currentWeight: Float? = null,
    val targetWeight: Float? = null,
    val startingWeight: Float? = null,
    val progressPercentage: Float = 0f,
    val remainingKg: Float = 0f,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showAddWeightDialog: Boolean = false
) {
    val hasData: Boolean
        get() = weightHistory.isNotEmpty()

    val isGoalAchieved: Boolean
        get() = currentWeight != null && targetWeight != null &&
                kotlin.math.abs(currentWeight - targetWeight) < 0.5f
}
