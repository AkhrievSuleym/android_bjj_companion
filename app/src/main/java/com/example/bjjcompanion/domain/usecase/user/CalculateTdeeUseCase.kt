package com.example.bjjcompanion.domain.usecase.user

import javax.inject.Inject

class CalculateTdeeUseCase @Inject constructor() {
    /**
     * Calculate Total Daily Energy Expenditure (TDEE)
     * MVP formula: weight (kg) × 30 kcal/kg
     *
     * @param weightKg Current weight in kilograms
     * @return Estimated TDEE in calories
     */
    operator fun invoke(weightKg: Float): Int {
        if (weightKg <= 0) {
            throw IllegalArgumentException("Weight must be positive")
        }
        return (weightKg * 30).toInt()
    }
}
