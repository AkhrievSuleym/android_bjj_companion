package com.example.bjjcompanion.di

import com.example.bjjcompanion.data.repository.NutritionRepositoryImpl
import com.example.bjjcompanion.data.repository.TechniqueRepositoryImpl
import com.example.bjjcompanion.data.repository.TrainingRepositoryImpl
import com.example.bjjcompanion.data.repository.UserRepositoryImpl
import com.example.bjjcompanion.domain.repository.NutritionRepository
import com.example.bjjcompanion.domain.repository.TechniqueRepository
import com.example.bjjcompanion.domain.repository.TrainingRepository
import com.example.bjjcompanion.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindNutritionRepository(
        nutritionRepositoryImpl: NutritionRepositoryImpl
    ): NutritionRepository

    @Binds
    @Singleton
    abstract fun bindTrainingRepository(
        trainingRepositoryImpl: TrainingRepositoryImpl
    ): TrainingRepository

    @Binds
    @Singleton
    abstract fun bindTechniqueRepository(
        techniqueRepositoryImpl: TechniqueRepositoryImpl
    ): TechniqueRepository
}
