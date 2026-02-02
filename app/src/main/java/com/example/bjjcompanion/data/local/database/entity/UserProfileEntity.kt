package com.example.bjjcompanion.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Long = 1L, // Always 1 - single user per device

    val name: String,

    val age: Int,

    val height: Float, // cm

    @ColumnInfo(name = "current_weight")
    val currentWeight: Float, // kg

    @ColumnInfo(name = "target_weight")
    val targetWeight: Float, // kg

    @ColumnInfo(name = "weight_class")
    val weightClass: String,

    val gender: String,

    @ColumnInfo(name = "daily_calories")
    val dailyCalories: Int, // TDEE calculation result

    @ColumnInfo(name = "created_at")
    val createdAt: Long // timestamp
)
