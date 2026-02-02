package com.example.bjjcompanion.domain.usecase.training

import com.example.bjjcompanion.domain.model.Training
import com.example.bjjcompanion.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

/**
 * Use case for retrieving trainings for a specific month.
 */
class GetTrainingsForMonthUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {
    /**
     * Gets all trainings for the specified month.
     *
     * @param year Year (e.g., 2024)
     * @param month Month (1-12)
     * @return Flow of training list for the month
     */
    operator fun invoke(year: Int, month: Int): Flow<List<Training>> {
        val startDate = LocalDate.of(year, month, 1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val endDate = startDate.let {
            LocalDate.of(year, month, 1)
                .plusMonths(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }

        return trainingRepository.getTrainingsForMonth(startDate, endDate)
    }
}
