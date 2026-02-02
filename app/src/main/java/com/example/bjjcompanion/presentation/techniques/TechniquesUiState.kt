package com.example.bjjcompanion.presentation.techniques

import com.example.bjjcompanion.domain.model.Technique
import com.example.bjjcompanion.domain.model.TechniqueCategory

/**
 * UI state for the techniques screen.
 */
data class TechniquesUiState(
    val techniques: List<Technique> = emptyList(),
    val selectedCategory: TechniqueCategory? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTechnique: Technique? = null
)
