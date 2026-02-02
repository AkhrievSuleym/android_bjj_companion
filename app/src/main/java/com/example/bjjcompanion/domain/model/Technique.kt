package com.example.bjjcompanion.domain.model

data class Technique(
    val id: Long = 0,
    val name: String,
    val category: TechniqueCategory,
    val description: String? = null,
    val createdAt: Long
)
