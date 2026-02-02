package com.example.bjjcompanion.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.domain.usecase.dashboard.GetDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(isLoading = true))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            getDashboardDataUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load dashboard data"
                        )
                    }
                }
                .collect { dashboardData ->
                    _uiState.update {
                        it.copy(
                            userProfile = dashboardData.userProfile,
                            currentWeight = dashboardData.currentWeight,
                            targetWeight = dashboardData.userProfile?.targetWeight,
                            weightProgress = calculateWeightProgress(
                                current = dashboardData.currentWeight,
                                target = dashboardData.userProfile?.targetWeight,
                                starting = dashboardData.recentWeightEntries.minByOrNull { it.date }?.weight
                            ),
                            recentWeightEntries = dashboardData.recentWeightEntries,
                            dailyNutrition = dashboardData.dailyNutrition,
                            caloriesTarget = dashboardData.userProfile?.dailyCalories ?: 0,
                            caloriesRemaining = calculateCaloriesRemaining(
                                target = dashboardData.userProfile?.dailyCalories ?: 0,
                                consumed = dashboardData.dailyNutrition.totalCalories
                            ),
                            foodLogsCount = dashboardData.foodLogsCount,
                            nextTraining = dashboardData.nextTraining,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isLoading = true) }
        loadDashboardData()
    }

    private fun calculateWeightProgress(current: Float?, target: Float?, starting: Float?): Float {
        if (current == null || target == null || starting == null) return 0f
        if (starting == target) return 100f

        val totalChange = target - starting
        val currentChange = current - starting

        return ((currentChange / totalChange) * 100f).coerceIn(0f, 100f)
    }

    private fun calculateCaloriesRemaining(target: Int, consumed: Float): Int {
        return (target - consumed.toInt()).coerceAtLeast(0)
    }
}
