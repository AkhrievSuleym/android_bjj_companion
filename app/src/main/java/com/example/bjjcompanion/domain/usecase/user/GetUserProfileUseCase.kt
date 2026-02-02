package com.example.bjjcompanion.domain.usecase.user

import com.example.bjjcompanion.domain.model.UserProfile
import com.example.bjjcompanion.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserProfile?> {
        return userRepository.getUserProfile()
    }
}
