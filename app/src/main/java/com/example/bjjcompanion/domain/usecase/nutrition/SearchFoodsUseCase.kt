package com.example.bjjcompanion.domain.usecase.nutrition

import com.example.bjjcompanion.domain.model.Food
import com.example.bjjcompanion.domain.repository.NutritionRepository
import javax.inject.Inject

class SearchFoodsUseCase @Inject constructor(
    private val nutritionRepository: NutritionRepository
) {
    suspend operator fun invoke(query: String): Result<List<Food>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }

        if (query.length < 2) {
            return Result.failure(IllegalArgumentException("Query must be at least 2 characters"))
        }

        return nutritionRepository.searchFoods(query)
    }
}
