package com.example.bjjcompanion.presentation.dashboard

import com.example.bjjcompanion.domain.model.Training
import com.example.bjjcompanion.domain.model.UserProfile
import com.example.bjjcompanion.domain.model.WeightEntry
import com.example.bjjcompanion.domain.repository.DailyNutrition

data class DashboardUiState(
    val userProfile: UserProfile? = null,
    val currentWeight: Float? = null,
    val targetWeight: Float? = null,
    val weightProgress: Float = 0f,
    val recentWeightEntries: List<WeightEntry> = emptyList(),
    val dailyNutrition: DailyNutrition = DailyNutrition(0f, 0f, 0f, 0f),
    val caloriesTarget: Int = 0,
    val caloriesRemaining: Int = 0,
    val foodLogsCount: Int = 0,
    val nextTraining: Training? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val hasWeightData: Boolean
        get() = recentWeightEntries.isNotEmpty()

    val hasNutritionData: Boolean
        get() = foodLogsCount > 0

    val hasTrainingData: Boolean
        get() = nextTraining != null

    val greetingName: String
        get() = userProfile?.name ?: "User"

    val weightDifference: Float
        get() {
            if (currentWeight == null || targetWeight == null) return 0f
            return targetWeight - currentWeight
        }

    val isOverCalories: Boolean
        get() = dailyNutrition.totalCalories > caloriesTarget

    val caloriesProgressPercentage: Float
        get() {
            if (caloriesTarget == 0) return 0f
            return (dailyNutrition.totalCalories / caloriesTarget * 100f).coerceIn(0f, 150f)
        }
}
