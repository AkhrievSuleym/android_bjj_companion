package com.example.bjjcompanion.domain.usecase.technique

import com.example.bjjcompanion.domain.model.Technique
import com.example.bjjcompanion.domain.repository.TechniqueRepository
import javax.inject.Inject

/**
 * Use case for adding a new technique with validation.
 */
class AddTechniqueUseCase @Inject constructor(
    private val techniqueRepository: TechniqueRepository
) {
    /**
     * Adds a technique after validation.
     *
     * @param technique The technique to add
     * @return Result with success or error message
     */
    suspend operator fun invoke(technique: Technique): Result<Unit> {
        return try {
            // Validate name
            if (technique.name.isBlank()) {
                return Result.failure(IllegalArgumentException("Name cannot be blank"))
            }

            // Validate description length if present
            technique.description?.let { description ->
                if (description.length > 2000) {
                    return Result.failure(
                        IllegalArgumentException("Description must be less than 2000 characters")
                    )
                }
            }

            techniqueRepository.addTechnique(technique)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
