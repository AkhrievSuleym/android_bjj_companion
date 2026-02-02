package com.example.bjjcompanion.domain.usecase.training

import com.example.bjjcompanion.domain.model.Training
import com.example.bjjcompanion.domain.repository.TrainingRepository
import javax.inject.Inject

/**
 * Use case for adding a new training session with validation.
 */
class AddTrainingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    /**
     * Adds a training session after validation.
     *
     * @param training The training session to add
     * @return Result with success or error message
     */
    suspend operator fun invoke(training: Training): Result<Unit> {
        return try {
            // Validate duration
            if (training.durationMinutes <= 0) {
                return Result.failure(IllegalArgumentException("Duration must be positive"))
            }

            // Validate notes length if present
            training.notes?.let { notes ->
                if (notes.length > 1000) {
                    return Result.failure(
                        IllegalArgumentException("Notes must be less than 1000 characters")
                    )
                }
            }

            trainingRepository.addTraining(training)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
