
package com.example.bjjcompanion.domain.model

data class FoodLog(
    val id: Long = 0,
    val fdcId: Int,
    val name: String,
    val calories: Float,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val grams: Float,
    val mealType: MealType,
    val date: Long
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}
