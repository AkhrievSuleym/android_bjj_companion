package com.example.bjjcompanion.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bjjcompanion.data.local.database.entity.TechniqueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TechniqueDao {
    @Insert
    suspend fun insertTechnique(technique: TechniqueEntity): Long

    @Update
    suspend fun updateTechnique(technique: TechniqueEntity)

    @Delete
    suspend fun deleteTechnique(technique: TechniqueEntity)

    @Query("DELETE FROM techniques WHERE id = :id")
    suspend fun deleteTechniqueById(id: Long)

    @Query("SELECT * FROM techniques ORDER BY created_at DESC")
    fun getAllTechniques(): Flow<List<TechniqueEntity>>

    @Query("SELECT * FROM techniques WHERE category = :category ORDER BY created_at DESC")
    fun getTechniquesByCategory(category: String): Flow<List<TechniqueEntity>>

    @Query("SELECT * FROM techniques WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY created_at DESC")
    fun searchTechniques(query: String): Flow<List<TechniqueEntity>>

    @Query("SELECT * FROM techniques WHERE id = :id")
    suspend fun getTechniqueById(id: Long): TechniqueEntity?
}
