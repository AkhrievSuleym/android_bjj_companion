package com.example.bjjcompanion.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_logs",
    indices = [
        Index(value = ["date"]),
        Index(value = ["fdc_id"])
    ]
)
data class FoodLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "fdc_id")
    val fdcId: Int, // USDA FoodData Central ID (0 for custom foods)

    val name: String,

    val calories: Float, // Total calories for portion

    val protein: Float, // Protein grams for portion

    val fat: Float, // Fat grams for portion

    val carbs: Float, // Carbs grams for portion

    val grams: Float, // Portion size

    @ColumnInfo(name = "meal_type")
    val mealType: String, // breakfast, lunch, dinner, snack

    val date: Long // timestamp (normalized to start of day)
)
