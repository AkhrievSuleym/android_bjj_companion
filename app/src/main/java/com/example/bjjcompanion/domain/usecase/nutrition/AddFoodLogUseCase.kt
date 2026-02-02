package com.example.bjjcompanion.domain.usecase.nutrition

import com.example.bjjcompanion.domain.model.Food
import com.example.bjjcompanion.domain.model.FoodLog
import com.example.bjjcompanion.domain.model.MealType
import com.example.bjjcompanion.domain.repository.NutritionRepository
import java.util.Calendar
import javax.inject.Inject

class AddFoodLogUseCase @Inject constructor(
    private val nutritionRepository: NutritionRepository
) {
    suspend operator fun invoke(
        food: Food,
        grams: Float,
        mealType: MealType,
        date: Long = System.currentTimeMillis()
    ): Result<Unit> {
        // Validation
        if (grams <= 0) {
            return Result.failure(IllegalArgumentException("Grams must be positive"))
        }

        if (grams > 10000) {
            return Result.failure(IllegalArgumentException("Grams cannot exceed 10kg"))
        }

        // Calculate nutrition values for the specified portion
        val multiplier = grams / 100f

        val foodLog = FoodLog(
            fdcId = food.fdcId,
            name = food.name,
            calories = food.caloriesPer100g * multiplier,
            protein = food.proteinPer100g * multiplier,
            fat = food.fatPer100g * multiplier,
            carbs = food.carbsPer100g * multiplier,
            grams = grams,
            mealType = mealType,
            date = normalizeDateToStartOfDay(date)
        )

        return try {
            nutritionRepository.addFoodLog(foodLog)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun normalizeDateToStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
