package com.example.bjjcompanion.presentation.techniques

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.domain.model.Technique
import com.example.bjjcompanion.domain.model.TechniqueCategory
import com.example.bjjcompanion.domain.usecase.technique.GetTechniquesUseCase
import com.example.bjjcompanion.domain.usecase.technique.SearchTechniquesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the techniques screen.
 */
@OptIn(FlowPreview::class)
@HiltViewModel
class TechniquesViewModel @Inject constructor(
    private val getTechniquesUseCase: GetTechniquesUseCase,
    private val searchTechniquesUseCase: SearchTechniquesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TechniquesUiState())
    val uiState: StateFlow<TechniquesUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")
    private val categoryFilterFlow = MutableStateFlow<TechniqueCategory?>(null)

    init {
        observeTechniques()
    }

    /**
     * Update the search query.
     */
    fun updateSearchQuery(query: String) {
        searchQueryFlow.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    /**
     * Select a category filter.
     */
    fun selectCategory(category: TechniqueCategory?) {
        categoryFilterFlow.value = category
        _uiState.update { it.copy(selectedCategory = category) }
    }

    /**
     * Select a technique to view details.
     */
    fun selectTechnique(technique: Technique) {
        _uiState.update { it.copy(selectedTechnique = technique) }
    }

    /**
     * Clear technique selection.
     */
    fun clearSelection() {
        _uiState.update { it.copy(selectedTechnique = null) }
    }

    private fun observeTechniques() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                searchQueryFlow.debounce(300),
                categoryFilterFlow
            ) { query, category ->
                Pair(query, category)
            }.flatMapLatest { (query, category) ->
                if (query.isNotBlank()) {
                    searchTechniquesUseCase(query)
                } else {
                    getTechniquesUseCase(category)
                }
            }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load techniques"
                        )
                    }
                }
                .collect { techniques ->
                    _uiState.update {
                        it.copy(
                            techniques = techniques,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
}
