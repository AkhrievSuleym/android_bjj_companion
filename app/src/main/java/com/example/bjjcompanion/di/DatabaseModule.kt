package com.example.bjjcompanion.di

import android.content.Context
import androidx.room.Room
import com.example.bjjcompanion.data.local.database.AppDatabase
import com.example.bjjcompanion.data.local.database.dao.FoodLogDao
import com.example.bjjcompanion.data.local.database.dao.TechniqueDao
import com.example.bjjcompanion.data.local.database.dao.TrainingDao
import com.example.bjjcompanion.data.local.database.dao.UserProfileDao
import com.example.bjjcompanion.data.local.database.dao.WeightEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "grappling_companion_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserProfileDao(database: AppDatabase): UserProfileDao {
        return database.userProfileDao()
    }

    @Provides
    @Singleton
    fun provideWeightEntryDao(database: AppDatabase): WeightEntryDao {
        return database.weightEntryDao()
    }

    @Provides
    @Singleton
    fun provideFoodLogDao(database: AppDatabase): FoodLogDao {
        return database.foodLogDao()
    }

    @Provides
    @Singleton
    fun provideTrainingDao(database: AppDatabase): TrainingDao {
        return database.trainingDao()
    }

    @Provides
    @Singleton
    fun provideTechniqueDao(database: AppDatabase): TechniqueDao {
        return database.techniqueDao()
    }
}
