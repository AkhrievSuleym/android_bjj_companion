package com.example.bjjcompanion.data.repository

import com.example.bjjcompanion.data.local.database.dao.TrainingDao
import com.example.bjjcompanion.data.local.database.entity.TrainingEntity
import com.example.bjjcompanion.domain.model.Training
import com.example.bjjcompanion.domain.model.TrainingType
import com.example.bjjcompanion.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val trainingDao: TrainingDao
) : TrainingRepository {

    override suspend fun addTraining(training: Training) {
        trainingDao.insertTraining(training.toEntity())
    }

    override suspend fun updateTraining(training: Training) {
        trainingDao.updateTraining(training.toEntity())
    }

    override suspend fun deleteTraining(training: Training) {
        trainingDao.deleteTrainingById(training.id)
    }

    override fun getTrainingsForMonth(startDate: Long, endDate: Long): Flow<List<Training>> {
        return trainingDao.getTrainingsForMonth(startDate, endDate).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTrainingsByType(type: String): Flow<List<Training>> {
        return trainingDao.getTrainingsByType(type).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getAllTrainings(): Flow<List<Training>> {
        return trainingDao.getAllTrainings().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getUpcomingTraining(currentDate: Long): Flow<Training?> {
        return trainingDao.getUpcomingTraining(currentDate).map { it?.toDomain() }
    }

    override fun getUpcomingTrainings(): Flow<List<Training>> {
        return trainingDao.getUpcomingTrainings().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getTrainingsForDate(date: Long): Flow<List<Training>> {
        return trainingDao.getTrainingsForDate(date).map { list ->
            list.map { it.toDomain() }
        }
    }

    // Mapping functions
    private fun TrainingEntity.toDomain() = Training(
        id = id,
        date = date,
        type = TrainingType.valueOf(type.uppercase()),
        durationMinutes = durationMinutes,
        notes = notes
    )

    private fun Training.toEntity() = TrainingEntity(
        id = id,
        date = date,
        type = type.name.lowercase(),
        durationMinutes = durationMinutes,
        notes = notes
    )
}
