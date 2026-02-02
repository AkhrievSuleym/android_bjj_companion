package com.example.bjjcompanion.domain.model

data class WeightEntry(
    val id: Long = 0,
    val weight: Float,
    val date: Long,
    val note: String? = null
)
