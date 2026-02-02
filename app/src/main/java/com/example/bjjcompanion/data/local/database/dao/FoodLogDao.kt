package com.example.bjjcompanion.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bjjcompanion.data.local.database.entity.FoodLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {
    @Insert
    suspend fun insertFoodLog(log: FoodLogEntity): Long

    @Update
    suspend fun updateFoodLog(log: FoodLogEntity)

    @Delete
    suspend fun deleteFoodLog(log: FoodLogEntity)

    @Query("DELETE FROM food_logs WHERE id = :id")
    suspend fun deleteFoodLogById(id: Long)

    @Query("SELECT * FROM food_logs WHERE date = :date ORDER BY meal_type ASC")
    fun getFoodLogsForDate(date: Long): Flow<List<FoodLogEntity>>

    @Query("SELECT * FROM food_logs WHERE date = :date AND meal_type = :mealType ORDER BY id DESC")
    fun getFoodLogsByMealType(date: Long, mealType: String): Flow<List<FoodLogEntity>>

    @Query("SELECT SUM(calories) FROM food_logs WHERE date = :date")
    suspend fun getTotalCaloriesForDate(date: Long): Float?

    @Query("SELECT SUM(protein) FROM food_logs WHERE date = :date")
    suspend fun getTotalProteinForDate(date: Long): Float?

    @Query("SELECT SUM(fat) FROM food_logs WHERE date = :date")
    suspend fun getTotalFatForDate(date: Long): Float?

    @Query("SELECT SUM(carbs) FROM food_logs WHERE date = :date")
    suspend fun getTotalCarbsForDate(date: Long): Float?

    @Query("DELETE FROM food_logs WHERE date < :cutoffDate")
    suspend fun deleteOldLogs(cutoffDate: Long)
}
