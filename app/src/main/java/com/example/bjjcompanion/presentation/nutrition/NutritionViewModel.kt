package com.example.bjjcompanion.presentation.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.domain.model.Food
import com.example.bjjcompanion.domain.model.FoodLog
import com.example.bjjcompanion.domain.model.MealType
import com.example.bjjcompanion.domain.repository.NutritionRepository
import com.example.bjjcompanion.domain.usecase.nutrition.AddFoodLogUseCase
import com.example.bjjcompanion.domain.usecase.nutrition.GetDailyNutritionUseCase
import com.example.bjjcompanion.domain.usecase.nutrition.SearchFoodsUseCase
import com.example.bjjcompanion.domain.usecase.user.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getDailyNutritionUseCase: GetDailyNutritionUseCase,
    private val searchFoodsUseCase: SearchFoodsUseCase,
    private val addFoodLogUseCase: AddFoodLogUseCase,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getUserProfileUseCase(),
                getDailyNutritionUseCase(_uiState.value.selectedDate)
            ) { profile, dailyData ->
                _uiState.update {
                    it.copy(
                        caloriesTarget = profile?.dailyCalories ?: 0,
                        dailyNutrition = dailyData.dailyNutrition,
                        foodLogs = dailyData.foodLogs,
                        isLoading = false
                    )
                }
            }.collect {}
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query, searchError = null) }

        // Debounce search
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            if (query.isNotBlank()) {
                searchFoods(query)
            } else {
                _uiState.update { it.copy(searchResults = emptyList()) }
            }
        }
    }

    private fun searchFoods(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearching = true, searchError = null) }

            searchFoodsUseCase(query)
                .onSuccess { foods ->
                    _uiState.update {
                        it.copy(
                            searchResults = foods,
                            isSearching = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            searchError = error.message ?: "Failed to search foods",
                            isSearching = false
                        )
                    }
                }
        }
    }

    fun showSearchDialog() {
        _uiState.update {
            it.copy(
                showSearchDialog = true,
                searchQuery = "",
                searchResults = emptyList(),
                searchError = null
            )
        }
    }

    fun hideSearchDialog() {
        _uiState.update {
            it.copy(
                showSearchDialog = false,
                searchQuery = "",
                searchResults = emptyList()
            )
        }
    }

    fun onFoodSelected(food: Food, mealType: MealType) {
        _uiState.update {
            it.copy(
                selectedFood = food,
                selectedMealType = mealType,
                showSearchDialog = false,
                showAddFoodDialog = true
            )
        }
    }

    fun hideAddFoodDialog() {
        _uiState.update {
            it.copy(
                showAddFoodDialog = false,
                selectedFood = null,
                errorMessage = null
            )
        }
    }

    fun addFoodLog(grams: Float) {
        val food = _uiState.value.selectedFood ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            addFoodLogUseCase(
                food = food,
                grams = grams,
                mealType = _uiState.value.selectedMealType,
                date = _uiState.value.selectedDate
            )
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            showAddFoodDialog = false,
                            selectedFood = null,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to add food log"
                        )
                    }
                }
        }
    }

    fun deleteFoodLog(foodLog: FoodLog) {
        viewModelScope.launch {
            nutritionRepository.deleteFoodLog(foodLog)
        }
    }

    fun onMealTypeSelected(mealType: MealType) {
        _uiState.update { it.copy(selectedMealType = mealType) }
    }
}
