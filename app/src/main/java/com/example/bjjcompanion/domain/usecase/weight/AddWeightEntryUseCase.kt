package com.example.bjjcompanion.domain.usecase.weight

import com.example.bjjcompanion.domain.model.WeightEntry
import com.example.bjjcompanion.domain.repository.UserRepository
import java.util.Calendar
import javax.inject.Inject

class AddWeightEntryUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(weightKg: Float, date: Long = System.currentTimeMillis()): Result<Unit> {
        // Validation: weight must be positive
        if (weightKg <= 0) {
            return Result.failure(IllegalArgumentException("Weight must be positive"))
        }

        // Validation: no future dates
        val normalizedDate = normalizeDateToStartOfDay(date)
        val today = normalizeDateToStartOfDay(System.currentTimeMillis())

        if (normalizedDate > today) {
            return Result.failure(IllegalArgumentException("Cannot add weight for future dates"))
        }

        // Weight must be within reasonable range (20-300 kg)
        if (weightKg < 20 || weightKg > 300) {
            return Result.failure(IllegalArgumentException("Weight must be between 20 and 300 kg"))
        }

        return try {
            val weightEntry = WeightEntry(
                weight = weightKg,
                date = normalizedDate
            )
            userRepository.addWeightEntry(weightEntry)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Normalize date to start of day (00:00:00.000)
     * This ensures one weight entry per day
     */
    private fun normalizeDateToStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
