package com.example.bjjcompanion.domain.usecase.training

import com.example.bjjcompanion.domain.model.Training
import com.example.bjjcompanion.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving the next upcoming training session.
 */
class GetUpcomingTrainingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    /**
     * Gets the next training session scheduled for today or future.
     *
     * @return Flow of the upcoming training, or null if none scheduled
     */
    operator fun invoke(): Flow<Training?> {
        val currentTime = System.currentTimeMillis()
        return trainingRepository.getUpcomingTraining(currentTime)
    }
}
