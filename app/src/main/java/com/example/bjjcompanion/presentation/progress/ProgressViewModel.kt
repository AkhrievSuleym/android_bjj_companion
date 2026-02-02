package com.example.bjjcompanion.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.domain.usecase.user.GetUserProfileUseCase
import com.example.bjjcompanion.domain.usecase.weight.AddWeightEntryUseCase
import com.example.bjjcompanion.domain.usecase.weight.GetWeightHistoryUseCase
import com.example.bjjcompanion.domain.usecase.weight.TimePeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getWeightHistoryUseCase: GetWeightHistoryUseCase,
    private val addWeightEntryUseCase: AddWeightEntryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                getUserProfileUseCase(),
                getWeightHistoryUseCase(_uiState.value.selectedPeriod)
            ) { profile, weightHistory ->
                val currentWeight = weightHistory.maxByOrNull { it.date }?.weight
                val startingWeight = weightHistory.minByOrNull { it.date }?.weight

                _uiState.update {
                    it.copy(
                        weightHistory = weightHistory,
                        currentWeight = currentWeight,
                        targetWeight = profile?.targetWeight,
                        startingWeight = startingWeight,
                        progressPercentage = calculateProgress(
                            current = currentWeight,
                            target = profile?.targetWeight,
                            starting = startingWeight
                        ),
                        remainingKg = calculateRemaining(
                            current = currentWeight,
                            target = profile?.targetWeight
                        ),
                        isLoading = false
                    )
                }
            }.collect {}
        }
    }

    fun onPeriodSelected(period: TimePeriod) {
        _uiState.update { it.copy(selectedPeriod = period, isLoading = true) }
        viewModelScope.launch {
            getWeightHistoryUseCase(period).collect { weightHistory ->
                val currentWeight = weightHistory.maxByOrNull { it.date }?.weight
                val startingWeight = weightHistory.minByOrNull { it.date }?.weight

                _uiState.update {
                    it.copy(
                        weightHistory = weightHistory,
                        currentWeight = currentWeight,
                        startingWeight = startingWeight,
                        progressPercentage = calculateProgress(
                            current = currentWeight,
                            target = it.targetWeight,
                            starting = startingWeight
                        ),
                        remainingKg = calculateRemaining(
                            current = currentWeight,
                            target = it.targetWeight
                        ),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun showAddWeightDialog() {
        _uiState.update { it.copy(showAddWeightDialog = true) }
    }

    fun hideAddWeightDialog() {
        _uiState.update { it.copy(showAddWeightDialog = false, errorMessage = null) }
    }

    fun addWeightEntry(weightKg: Float) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            addWeightEntryUseCase(weightKg)
                .onSuccess {
                    _uiState.update { it.copy(showAddWeightDialog = false, isLoading = false) }
                    // Data will be automatically refreshed through Flow
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to add weight entry"
                        )
                    }
                }
        }
    }

    private fun calculateProgress(current: Float?, target: Float?, starting: Float?): Float {
        if (current == null || target == null || starting == null) return 0f
        if (starting == target) return 100f

        val totalChange = target - starting
        val currentChange = current - starting

        return ((currentChange / totalChange) * 100f).coerceIn(0f, 100f)
    }

    private fun calculateRemaining(current: Float?, target: Float?): Float {
        if (current == null || target == null) return 0f
        return abs(target - current)
    }
}
