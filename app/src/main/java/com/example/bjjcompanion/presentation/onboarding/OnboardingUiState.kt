package com.example.bjjcompanion.presentation.onboarding

data class OnboardingUiState(
    val currentStep: Int = 0, // 0 = welcome, 1 = profile form, 2 = TDEE result
    val name: String = "",
    val age: String = "",
    val height: String = "",
    val currentWeight: String = "",
    val targetWeight: String = "",
    val weightClass: String = "",
    val gender: Gender = Gender.MALE,
    val calculatedTdee: Int = 0,
    val nameError: String? = null,
    val ageError: String? = null,
    val heightError: String? = null,
    val currentWeightError: String? = null,
    val targetWeightError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}
