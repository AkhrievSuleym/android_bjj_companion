package com.example.bjjcompanion.domain.usecase.dashboard

import com.example.bjjcompanion.domain.model.UserProfile
import com.example.bjjcompanion.domain.model.WeightEntry
import com.example.bjjcompanion.domain.repository.NutritionRepository
import com.example.bjjcompanion.domain.repository.TrainingRepository
import com.example.bjjcompanion.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val nutritionRepository: NutritionRepository,
    private val trainingRepository: TrainingRepository
) {
    operator fun invoke(): Flow<DashboardData> {
        val today = getTodayTimestamp()
        val weekAgo = getWeekAgoTimestamp()

        return combine(
            userRepository.getUserProfile(),
            userRepository.getWeightEntriesForRange(weekAgo, today),
            nutritionRepository.getFoodLogsForDate(today),
            trainingRepository.getUpcomingTrainings()
        ) { profile, weightHistory, foodLogs, upcomingTrainings ->
            val dailyNutrition = nutritionRepository.getDailyNutritionTotals(today)

            DashboardData(
                userProfile = profile,
                recentWeightEntries = weightHistory.sortedByDescending { it.date }.take(7),
                currentWeight = weightHistory.maxByOrNull { it.date }?.weight,
                foodLogsCount = foodLogs.size,
                dailyNutrition = dailyNutrition,
                nextTraining = upcomingTrainings.firstOrNull()
            )
        }
    }

    private fun getTodayTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getWeekAgoTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        return calendar.timeInMillis
    }
}

data class DashboardData(
    val userProfile: UserProfile?,
    val recentWeightEntries: List<WeightEntry>,
    val currentWeight: Float?,
    val foodLogsCount: Int,
    val dailyNutrition: com.example.bjjcompanion.domain.repository.DailyNutrition,
    val nextTraining: com.example.bjjcompanion.domain.model.Training?
)
