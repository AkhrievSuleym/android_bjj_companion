package com.example.bjjcompanion.domain.model

data class Training(
    val id: Long = 0,
    val date: Long,
    val type: TrainingType,
    val durationMinutes: Int,
    val notes: String? = null
)
