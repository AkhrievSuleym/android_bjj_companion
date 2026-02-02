package com.example.bjjcompanion.data.repository

import com.example.bjjcompanion.data.local.database.dao.UserProfileDao
import com.example.bjjcompanion.data.local.database.dao.WeightEntryDao
import com.example.bjjcompanion.data.local.database.entity.UserProfileEntity
import com.example.bjjcompanion.data.local.database.entity.WeightEntryEntity
import com.example.bjjcompanion.domain.model.UserProfile
import com.example.bjjcompanion.domain.model.WeightEntry
import com.example.bjjcompanion.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val weightEntryDao: WeightEntryDao
) : UserRepository {

    override fun getUserProfile(): Flow<UserProfile?> {
        return userProfileDao.getUserProfile().map { it?.toDomain() }
    }

    override suspend fun getUserProfileOnce(): UserProfile? {
        return userProfileDao.getUserProfileOnce()?.toDomain()
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(profile.toEntity())
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        userProfileDao.updateProfile(profile.toEntity())
    }

    override suspend fun deleteUserProfile() {
        userProfileDao.deleteProfile()
    }

    override suspend fun addWeightEntry(weightEntry: WeightEntry) {
        weightEntryDao.insertWeightEntry(weightEntry.toEntity())
    }

    override fun getWeightEntriesForRange(startDate: Long, endDate: Long): Flow<List<WeightEntry>> {
        return weightEntryDao.getWeightEntriesForRange(startDate, endDate).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteWeightEntry(weightEntry: WeightEntry) {
        weightEntryDao.deleteWeightEntry(weightEntry.toEntity())
    }

    // Mapping functions
    private fun UserProfileEntity.toDomain() = UserProfile(
        id = id,
        name = name,
        age = age,
        height = height,
        currentWeight = currentWeight,
        targetWeight = targetWeight,
        weightClass = weightClass,
        gender = gender,
        dailyCalories = dailyCalories,
        createdAt = createdAt
    )

    private fun UserProfile.toEntity() = UserProfileEntity(
        id = id,
        name = name,
        age = age,
        height = height,
        currentWeight = currentWeight,
        targetWeight = targetWeight,
        weightClass = weightClass,
        gender = gender,
        dailyCalories = dailyCalories,
        createdAt = createdAt
    )

    private fun WeightEntryEntity.toDomain() = WeightEntry(
        id = id,
        weight = weight,
        date = date
    )

    private fun WeightEntry.toEntity() = WeightEntryEntity(
        id = id,
        weight = weight,
        date = date
    )
}
