package com.example.bjjcompanion.domain.model

data class UserProfile(
    val id: Long = 1L,
    val name: String,
    val age: Int,
    val height: Float,
    val currentWeight: Float,
    val targetWeight: Float,
    val weightClass: String,
    val gender: String,
    val dailyCalories: Int,
    val createdAt: Long = System.currentTimeMillis()
)
