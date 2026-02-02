package com.example.bjjcompanion.domain.usecase.user

import com.example.bjjcompanion.domain.model.UserProfile
import com.example.bjjcompanion.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(profile: UserProfile): Result<Unit> {
        // Input validation
        if (profile.name.isBlank()) {
            return Result.failure(IllegalArgumentException("Name cannot be empty"))
        }

        if (profile.currentWeight <= 0) {
            return Result.failure(IllegalArgumentException("Current weight must be positive"))
        }

        if (profile.targetWeight <= 0) {
            return Result.failure(IllegalArgumentException("Target weight must be positive"))
        }

        if (profile.height <= 0) {
            return Result.failure(IllegalArgumentException("Height must be positive"))
        }

        if (profile.age <= 0 || profile.age > 120) {
            return Result.failure(IllegalArgumentException("Age must be between 1 and 120"))
        }

        return try {
            userRepository.saveUserProfile(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
