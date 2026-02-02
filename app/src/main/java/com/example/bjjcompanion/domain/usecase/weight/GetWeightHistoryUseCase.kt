package com.example.bjjcompanion.domain.usecase.weight

import com.example.bjjcompanion.domain.model.WeightEntry
import com.example.bjjcompanion.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class GetWeightHistoryUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(period: TimePeriod): Flow<List<WeightEntry>> {
        val endDate = System.currentTimeMillis()
        val startDate = calculateStartDate(endDate, period)

        return userRepository.getWeightEntriesForRange(startDate, endDate)
    }

    private fun calculateStartDate(endDate: Long, period: TimePeriod): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = endDate

        when (period) {
            TimePeriod.WEEK -> calendar.add(Calendar.DAY_OF_YEAR, -7)
            TimePeriod.MONTH -> calendar.add(Calendar.MONTH, -1)
            TimePeriod.THREE_MONTHS -> calendar.add(Calendar.MONTH, -3)
            TimePeriod.SIX_MONTHS -> calendar.add(Calendar.MONTH, -6)
            TimePeriod.YEAR -> calendar.add(Calendar.YEAR, -1)
            TimePeriod.ALL -> calendar.add(Calendar.YEAR, -10) // Go back 10 years for "all"
        }

        return calendar.timeInMillis
    }
}

enum class TimePeriod {
    WEEK,
    MONTH,
    THREE_MONTHS,
    SIX_MONTHS,
    YEAR,
    ALL
}
