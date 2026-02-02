package com.example.bjjcompanion.domain.repository

import com.example.bjjcompanion.domain.model.Training
import kotlinx.coroutines.flow.Flow

interface TrainingRepository {
    suspend fun addTraining(training: Training)
    suspend fun updateTraining(training: Training)
    suspend fun deleteTraining(training: Training)
    fun getTrainingsForMonth(startDate: Long, endDate: Long): Flow<List<Training>>
    fun getTrainingsByType(type: String): Flow<List<Training>>
    fun getAllTrainings(): Flow<List<Training>>
    fun getUpcomingTraining(currentDate: Long): Flow<Training?>
    fun getUpcomingTrainings(): Flow<List<Training>>
    fun getTrainingsForDate(date: Long): Flow<List<Training>>
}
