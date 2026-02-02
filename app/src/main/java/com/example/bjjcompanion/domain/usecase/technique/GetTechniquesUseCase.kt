package com.example.bjjcompanion.domain.usecase.technique

import com.example.bjjcompanion.domain.model.Technique
import com.example.bjjcompanion.domain.model.TechniqueCategory
import com.example.bjjcompanion.domain.repository.TechniqueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving techniques with optional category filter.
 */
class GetTechniquesUseCase @Inject constructor(
    private val techniqueRepository: TechniqueRepository
) {
    /**
     * Gets all techniques, optionally filtered by category.
     *
     * @param category Optional category filter
     * @return Flow of techniques
     */
    operator fun invoke(category: TechniqueCategory? = null): Flow<List<Technique>> {
        return if (category != null) {
            techniqueRepository.getTechniquesByCategory(category.name)
        } else {
            techniqueRepository.getAllTechniques()
        }
    }
}
