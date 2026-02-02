package com.example.bjjcompanion.data.repository

import com.example.bjjcompanion.BuildConfig
import com.example.bjjcompanion.data.local.database.dao.FoodLogDao
import com.example.bjjcompanion.data.local.database.entity.FoodLogEntity
import com.example.bjjcompanion.data.remote.api.UsdaFoodApi
import com.example.bjjcompanion.domain.model.Food
import com.example.bjjcompanion.domain.model.FoodLog
import com.example.bjjcompanion.domain.model.MealType
import com.example.bjjcompanion.domain.repository.DailyNutrition
import com.example.bjjcompanion.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NutritionRepositoryImpl @Inject constructor(
    private val foodLogDao: FoodLogDao,
    private val usdaFoodApi: UsdaFoodApi
) : NutritionRepository {

    override suspend fun searchFoods(query: String): Result<List<Food>> {
        return try {
            val response = usdaFoodApi.searchFoods(
                apiKey = BuildConfig.USDA_API_KEY,
                query = query,
                pageSize = 25
            )

            val foods = response.foods.map { foodDto ->
                // Extract key nutrients (Energy, Protein, Fat, Carbohydrate)
                val nutrients = foodDto.foodNutrients
                val calories = nutrients.find { it.nutrientName.contains("Energy", ignoreCase = true) }?.value ?: 0f
                val protein = nutrients.find { it.nutrientName.contains("Protein", ignoreCase = true) }?.value ?: 0f
                val fat = nutrients.find { it.nutrientName.contains("Total lipid", ignoreCase = true) ||
                                                 it.nutrientName.contains("Fat", ignoreCase = true) }?.value ?: 0f
                val carbs = nutrients.find { it.nutrientName.contains("Carbohydrate", ignoreCase = true) }?.value ?: 0f

                Food(
                    fdcId = foodDto.fdcId,
                    name = foodDto.description,
                    caloriesPer100g = calories,
                    proteinPer100g = protein,
                    fatPer100g = fat,
                    carbsPer100g = carbs,
                    isCustom = false
                )
            }

            Result.success(foods)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addFoodLog(foodLog: FoodLog) {
        foodLogDao.insertFoodLog(foodLog.toEntity())
    }

    override suspend fun updateFoodLog(foodLog: FoodLog) {
        foodLogDao.updateFoodLog(foodLog.toEntity())
    }

    override suspend fun deleteFoodLog(foodLog: FoodLog) {
        foodLogDao.deleteFoodLogById(foodLog.id)
    }

    override fun getFoodLogsForDate(date: Long): Flow<List<FoodLog>> {
        return foodLogDao.getFoodLogsForDate(date).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getDailyNutritionTotals(date: Long): DailyNutrition {
        return DailyNutrition(
            totalCalories = foodLogDao.getTotalCaloriesForDate(date) ?: 0f,
            totalProtein = foodLogDao.getTotalProteinForDate(date) ?: 0f,
            totalFat = foodLogDao.getTotalFatForDate(date) ?: 0f,
            totalCarbs = foodLogDao.getTotalCarbsForDate(date) ?: 0f
        )
    }

    // Mapping functions
    private fun FoodLogEntity.toDomain() = FoodLog(
        id = id,
        fdcId = fdcId,
        name = name,
        calories = calories,
        protein = protein,
        fat = fat,
        carbs = carbs,
        grams = grams,
        mealType = MealType.valueOf(mealType.uppercase()),
        date = date
    )

    private fun FoodLog.toEntity() = FoodLogEntity(
        id = id,
        fdcId = fdcId,
        name = name,
        calories = calories,
        protein = protein,
        fat = fat,
        carbs = carbs,
        grams = grams,
        mealType = mealType.name.lowercase(),
        date = date
    )
}
