package com.example.bjjcompanion.domain.usecase.nutrition

import com.example.bjjcompanion.domain.model.FoodLog
import com.example.bjjcompanion.domain.repository.DailyNutrition
import com.example.bjjcompanion.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetDailyNutritionUseCase @Inject constructor(
    private val nutritionRepository: NutritionRepository
) {
    operator fun invoke(date: Long = System.currentTimeMillis()): Flow<DailyNutritionData> {
        val normalizedDate = normalizeDateToStartOfDay(date)

        return nutritionRepository.getFoodLogsForDate(normalizedDate).map { foodLogs ->
            val dailyNutrition = nutritionRepository.getDailyNutritionTotals(normalizedDate)

            DailyNutritionData(
                foodLogs = foodLogs,
                dailyNutrition = dailyNutrition,
                date = normalizedDate
            )
        }
    }

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

data class DailyNutritionData(
    val foodLogs: List<FoodLog>,
    val dailyNutrition: DailyNutrition,
    val date: Long
)
