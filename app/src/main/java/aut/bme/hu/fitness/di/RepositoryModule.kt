package aut.bme.hu.fitness.di

import aut.bme.hu.fitness.api.AuthenticatedApiService
import aut.bme.hu.fitness.domain.repository.CalorieIntakeRepository
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.repository.ExerciseRepository
import aut.bme.hu.fitness.domain.repository.StepsRepository
import aut.bme.hu.fitness.domain.repository.impl.CalorieIntakeRepositoryImpl
import aut.bme.hu.fitness.domain.repository.impl.UserProfileRepositoryImpl
import aut.bme.hu.fitness.domain.repository.impl.ExerciseRepositoryImpl
import aut.bme.hu.fitness.domain.repository.impl.StepsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCalorieIntakeRepository(
        authenticatedApiService: AuthenticatedApiService
    ): CalorieIntakeRepository {
        return CalorieIntakeRepositoryImpl(authenticatedApiService)
    }

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        authenticatedApiService: AuthenticatedApiService
    ): UserProfileRepository {
        return UserProfileRepositoryImpl(authenticatedApiService)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(
        authenticatedApiService: AuthenticatedApiService
    ): ExerciseRepository {
        return ExerciseRepositoryImpl(authenticatedApiService)
    }

    @Provides
    @Singleton
    fun provideStepsRepository(
        authenticatedApiService: AuthenticatedApiService
    ): StepsRepository {
        return StepsRepositoryImpl(authenticatedApiService)
    }


}