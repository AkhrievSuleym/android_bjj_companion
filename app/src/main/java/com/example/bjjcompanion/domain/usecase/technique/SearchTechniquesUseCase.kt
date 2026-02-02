package com.example.bjjcompanion.domain.usecase.technique

import com.example.bjjcompanion.domain.model.Technique
import com.example.bjjcompanion.domain.repository.TechniqueRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for searching techniques by name or description.
 */
class SearchTechniquesUseCase @Inject constructor(
    private val techniqueRepository: TechniqueRepository
) {
    /**
     * Searches techniques by query string.
     *
     * @param query Search query (searches name and description)
     * @return Flow of matching techniques
     */
    operator fun invoke(query: String): Flow<List<Technique>> {
        return if (query.isBlank()) {
            techniqueRepository.getAllTechniques()
        } else {
            techniqueRepository.searchTechniques(query)
        }
    }
}
