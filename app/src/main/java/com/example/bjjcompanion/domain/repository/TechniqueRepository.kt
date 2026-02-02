package com.example.bjjcompanion.domain.repository

import com.example.bjjcompanion.domain.model.Technique
import kotlinx.coroutines.flow.Flow

interface TechniqueRepository {
    suspend fun addTechnique(technique: Technique)
    suspend fun updateTechnique(technique: Technique)
    suspend fun deleteTechnique(technique: Technique)
    fun getAllTechniques(): Flow<List<Technique>>
    fun getTechniquesByCategory(category: String): Flow<List<Technique>>
    fun searchTechniques(query: String): Flow<List<Technique>>
    suspend fun getTechniqueById(id: Long): Technique?
}
