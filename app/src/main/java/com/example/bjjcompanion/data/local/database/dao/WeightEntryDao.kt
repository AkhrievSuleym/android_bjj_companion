package com.example.bjjcompanion.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bjjcompanion.data.local.database.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightEntry(entry: WeightEntryEntity): Long

    @Update
    suspend fun updateWeightEntry(entry: WeightEntryEntity)

    @Delete
    suspend fun deleteWeightEntry(entry: WeightEntryEntity)

    @Query("DELETE FROM weight_entries WHERE id = :id")
    suspend fun deleteWeightEntryById(id: Long)

    @Query("SELECT * FROM weight_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getWeightEntriesForRange(startDate: Long, endDate: Long): Flow<List<WeightEntryEntity>>

    @Query("SELECT * FROM weight_entries ORDER BY date DESC LIMIT 1")
    fun getLatestWeightEntry(): Flow<WeightEntryEntity?>

    @Query("SELECT * FROM weight_entries ORDER BY date DESC LIMIT 1")
    suspend fun getLatestWeightEntryOnce(): WeightEntryEntity?

    @Query("SELECT * FROM weight_entries ORDER BY date DESC")
    fun getAllWeightEntries(): Flow<List<WeightEntryEntity>>

    @Query("SELECT * FROM weight_entries WHERE date = :date")
    suspend fun getWeightEntryByDate(date: Long): WeightEntryEntity?
}
