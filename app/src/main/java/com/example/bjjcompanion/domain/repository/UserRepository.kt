package com.example.bjjcompanion.domain.repository

import com.example.bjjcompanion.domain.model.UserProfile
import com.example.bjjcompanion.domain.model.WeightEntry
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(): Flow<UserProfile?>
    suspend fun getUserProfileOnce(): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun updateUserProfile(profile: UserProfile)
    suspend fun deleteUserProfile()

    // Weight tracking
    suspend fun addWeightEntry(weightEntry: WeightEntry)
    fun getWeightEntriesForRange(startDate: Long, endDate: Long): Flow<List<WeightEntry>>
    suspend fun deleteWeightEntry(weightEntry: WeightEntry)
}
