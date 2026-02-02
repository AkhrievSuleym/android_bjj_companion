package com.example.bjjcompanion.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FoodSearchResponse(
    val totalHits: Int,
    val currentPage: Int,
    val totalPages: Int,
    val foods: List<FoodDto>
)

@Serializable
data class FoodDto(
    val fdcId: Int,
    val description: String,
    val dataType: String? = null,
    val foodNutrients: List<FoodNutrientDto>
)
