package com.example.bjjcompanion.presentation.nutrition

import com.example.bjjcompanion.domain.model.Food
import com.example.bjjcompanion.domain.model.FoodLog
import com.example.bjjcompanion.domain.model.MealType
import com.example.bjjcompanion.domain.repository.DailyNutrition

data class NutritionUiState(
    val selectedDate: Long = System.currentTimeMillis(),
    val dailyNutrition: DailyNutrition = DailyNutrition(0f, 0f, 0f, 0f),
    val foodLogs: List<FoodLog> = emptyList(),
    val caloriesTarget: Int = 0,
    val searchQuery: String = "",
    val searchResults: List<Food> = emptyList(),
    val isSearching: Boolean = false,
    val searchError: String? = null,
    val showSearchDialog: Boolean = false,
    val showAddFoodDialog: Boolean = false,
    val selectedFood: Food? = null,
    val selectedMealType: MealType = MealType.BREAKFAST,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val hasData: Boolean
        get() = foodLogs.isNotEmpty()

    val caloriesRemaining: Int
        get() = (caloriesTarget - dailyNutrition.totalCalories).toInt()

    val caloriesPercentage: Float
        get() {
            if (caloriesTarget == 0) return 0f
            return (dailyNutrition.totalCalories / caloriesTarget * 100f).coerceIn(0f, 150f)
        }

    val isOverCalories: Boolean
        get() = dailyNutrition.totalCalories > caloriesTarget

    val breakfastLogs: List<FoodLog>
        get() = foodLogs.filter { it.mealType == MealType.BREAKFAST }

    val lunchLogs: List<FoodLog>
        get() = foodLogs.filter { it.mealType == MealType.LUNCH }

    val dinnerLogs: List<FoodLog>
        get() = foodLogs.filter { it.mealType == MealType.DINNER }

    val snackLogs: List<FoodLog>
        get() = foodLogs.filter { it.mealType == MealType.SNACK }
}
