package com.example.bjjcompanion.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bjjcompanion.data.local.database.entity.TrainingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingDao {
    @Insert
    suspend fun insertTraining(training: TrainingEntity): Long

    @Update
    suspend fun updateTraining(training: TrainingEntity)

    @Delete
    suspend fun deleteTraining(training: TrainingEntity)

    @Query("DELETE FROM trainings WHERE id = :id")
    suspend fun deleteTrainingById(id: Long)

    @Query("SELECT * FROM trainings WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTrainingsForMonth(startDate: Long, endDate: Long): Flow<List<TrainingEntity>>

    @Query("SELECT * FROM trainings WHERE type = :type ORDER BY date DESC")
    fun getTrainingsByType(type: String): Flow<List<TrainingEntity>>

    @Query("SELECT * FROM trainings ORDER BY date DESC")
    fun getAllTrainings(): Flow<List<TrainingEntity>>

    @Query("SELECT * FROM trainings WHERE date >= :currentDate ORDER BY date ASC LIMIT 1")
    fun getUpcomingTraining(currentDate: Long): Flow<TrainingEntity?>

    @Query("SELECT * FROM trainings WHERE date >= :currentDate ORDER BY date ASC LIMIT 5")
    fun getUpcomingTrainings(currentDate: Long = System.currentTimeMillis()): Flow<List<TrainingEntity>>

    @Query("SELECT * FROM trainings WHERE date = :date")
    fun getTrainingsForDate(date: Long): Flow<List<TrainingEntity>>
}
