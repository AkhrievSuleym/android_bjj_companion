package com.example.bjjcompanion.domain.repository

import com.example.bjjcompanion.domain.model.Food
import com.example.bjjcompanion.domain.model.FoodLog
import kotlinx.coroutines.flow.Flow

interface NutritionRepository {
    suspend fun searchFoods(query: String): Result<List<Food>>
    suspend fun addFoodLog(foodLog: FoodLog)
    suspend fun updateFoodLog(foodLog: FoodLog)
    suspend fun deleteFoodLog(foodLog: FoodLog)
    fun getFoodLogsForDate(date: Long): Flow<List<FoodLog>>
    suspend fun getDailyNutritionTotals(date: Long): DailyNutrition
}

data class DailyNutrition(
    val totalCalories: Float,
    val totalProtein: Float,
    val totalFat: Float,
    val totalCarbs: Float
)
