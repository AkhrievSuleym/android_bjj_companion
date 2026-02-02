package com.example.bjjcompanion.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bjjcompanion.data.local.database.dao.FoodLogDao
import com.example.bjjcompanion.data.local.database.dao.TechniqueDao
import com.example.bjjcompanion.data.local.database.dao.TrainingDao
import com.example.bjjcompanion.data.local.database.dao.UserProfileDao
import com.example.bjjcompanion.data.local.database.dao.WeightEntryDao
import com.example.bjjcompanion.data.local.database.entity.FoodLogEntity
import com.example.bjjcompanion.data.local.database.entity.TechniqueEntity
import com.example.bjjcompanion.data.local.database.entity.TrainingEntity
import com.example.bjjcompanion.data.local.database.entity.UserProfileEntity
import com.example.bjjcompanion.data.local.database.entity.WeightEntryEntity

@Database(
    entities = [
        UserProfileEntity::class,
        WeightEntryEntity::class,
        FoodLogEntity::class,
        TrainingEntity::class,
        TechniqueEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun weightEntryDao(): WeightEntryDao
    abstract fun foodLogDao(): FoodLogDao
    abstract fun trainingDao(): TrainingDao
    abstract fun techniqueDao(): TechniqueDao
}
