package com.example.bjjcompanion.presentation.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.domain.usecase.training.GetTrainingsForMonthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * ViewModel for the training calendar screen.
 */
@HiltViewModel
class TrainingCalendarViewModel @Inject constructor(
    private val getTrainingsForMonthUseCase: GetTrainingsForMonthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingCalendarUiState())
    val uiState: StateFlow<TrainingCalendarUiState> = _uiState.asStateFlow()

    init {
        loadTrainingsForCurrentMonth()
    }

    /**
     * Navigate to the previous month.
     */
    fun previousMonth() {
        val newMonth = _uiState.value.currentMonth.minusMonths(1)
        _uiState.update { it.copy(currentMonth = newMonth, selectedDate = null) }
        loadTrainingsForMonth(newMonth)
    }

    /**
     * Navigate to the next month.
     */
    fun nextMonth() {
        val newMonth = _uiState.value.currentMonth.plusMonths(1)
        _uiState.update { it.copy(currentMonth = newMonth, selectedDate = null) }
        loadTrainingsForMonth(newMonth)
    }

    /**
     * Select a specific date and filter trainings for that date.
     */
    fun selectDate(date: LocalDate) {
        val trainingsForDate = _uiState.value.trainings.filter { training ->
            val trainingDate = LocalDate.ofInstant(
                java.time.Instant.ofEpochMilli(training.date),
                ZoneId.systemDefault()
            )
            trainingDate == date
        }

        _uiState.update {
            it.copy(
                selectedDate = date,
                selectedDateTrainings = trainingsForDate
            )
        }
    }

    /**
     * Clear date selection.
     */
    fun clearSelection() {
        _uiState.update { it.copy(selectedDate = null, selectedDateTrainings = emptyList()) }
    }

    /**
     * Refresh trainings for current month.
     */
    fun refresh() {
        loadTrainingsForMonth(_uiState.value.currentMonth)
    }

    private fun loadTrainingsForCurrentMonth() {
        loadTrainingsForMonth(_uiState.value.currentMonth)
    }

    private fun loadTrainingsForMonth(month: LocalDate) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getTrainingsForMonthUseCase(month.year, month.monthValue)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load trainings"
                        )
                    }
                }
                .collect { trainings ->
                    _uiState.update {
                        it.copy(
                            trainings = trainings,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
}
