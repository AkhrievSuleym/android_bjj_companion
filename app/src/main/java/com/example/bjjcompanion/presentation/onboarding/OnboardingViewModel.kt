package com.example.bjjcompanion.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bjjcompanion.domain.model.UserProfile
import com.example.bjjcompanion.domain.usecase.user.CalculateTdeeUseCase
import com.example.bjjcompanion.domain.usecase.user.SaveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val calculateTdeeUseCase: CalculateTdeeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun onAgeChanged(age: String) {
        _uiState.update { it.copy(age = age, ageError = null) }
    }

    fun onHeightChanged(height: String) {
        _uiState.update { it.copy(height = height, heightError = null) }
    }

    fun onCurrentWeightChanged(weight: String) {
        _uiState.update { it.copy(currentWeight = weight, currentWeightError = null) }
    }

    fun onTargetWeightChanged(weight: String) {
        _uiState.update { it.copy(targetWeight = weight, targetWeightError = null) }
    }

    fun onWeightClassChanged(weightClass: String) {
        _uiState.update { it.copy(weightClass = weightClass) }
    }

    fun onGenderChanged(gender: Gender) {
        _uiState.update { it.copy(gender = gender) }
    }

    fun nextStep() {
        val currentStep = _uiState.value.currentStep
        if (currentStep == 0) {
            // Move from welcome to profile form
            _uiState.update { it.copy(currentStep = 1) }
        } else if (currentStep == 1) {
            // Validate and calculate TDEE
            if (validateInputs()) {
                calculateTdee()
            }
        }
    }

    fun previousStep() {
        val currentStep = _uiState.value.currentStep
        if (currentStep > 0) {
            _uiState.update { it.copy(currentStep = currentStep - 1, errorMessage = null) }
        }
    }

    private fun validateInputs(): Boolean {
        val state = _uiState.value
        var isValid = true

        // Validate name
        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Name is required") }
            isValid = false
        }

        // Validate age
        val age = state.age.toIntOrNull()
        if (age == null || age <= 0 || age > 120) {
            _uiState.update { it.copy(ageError = "Enter a valid age (1-120)") }
            isValid = false
        }

        // Validate height
        val height = state.height.toFloatOrNull()
        if (height == null || height <= 0) {
            _uiState.update { it.copy(heightError = "Enter a valid height") }
            isValid = false
        }

        // Validate current weight
        val currentWeight = state.currentWeight.toFloatOrNull()
        if (currentWeight == null || currentWeight <= 0) {
            _uiState.update { it.copy(currentWeightError = "Enter a valid weight") }
            isValid = false
        }

        // Validate target weight
        val targetWeight = state.targetWeight.toFloatOrNull()
        if (targetWeight == null || targetWeight <= 0) {
            _uiState.update { it.copy(targetWeightError = "Enter a valid target weight") }
            isValid = false
        }

        return isValid
    }

    private fun calculateTdee() {
        val state = _uiState.value
        val currentWeight = state.currentWeight.toFloatOrNull() ?: return

        try {
            val tdee = calculateTdeeUseCase(currentWeight)
            _uiState.update {
                it.copy(
                    calculatedTdee = tdee,
                    currentStep = 2
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Error calculating TDEE: ${e.message}") }
        }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.currentStep != 2) {
            _uiState.update { it.copy(errorMessage = "Please complete all steps first") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            val profile = UserProfile(
                id = 1, // Single profile per device
                name = state.name,
                age = state.age.toInt(),
                height = state.height.toFloat(),
                currentWeight = state.currentWeight.toFloat(),
                targetWeight = state.targetWeight.toFloat(),
                weightClass = state.weightClass,
                gender = state.gender.name.lowercase(),
                dailyCalories = state.calculatedTdee,
                createdAt = System.currentTimeMillis()
            )

            saveUserProfileUseCase(profile)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to save profile: ${error.message}"
                        )
                    }
                }
        }
    }
}
