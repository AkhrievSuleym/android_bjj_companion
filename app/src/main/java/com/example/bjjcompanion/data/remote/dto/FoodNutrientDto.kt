package com.example.bjjcompanion.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FoodNutrientDto(
    val nutrientId: Int,
    val nutrientName: String,
    val unitName: String,
    val value: Float
)
