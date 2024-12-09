package aut.bme.hu.fitness.di

import aut.bme.hu.fitness.domain.api.ApiService
import aut.bme.hu.fitness.domain.repository.CalorieIntakeRepository
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.repository.impl.CalorieIntakeRepositoryImpl
import aut.bme.hu.fitness.domain.repository.impl.UserProfileRepositoryImpl
import aut.bme.hu.fitness.domain.service.AuthService
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
        apiService: ApiService, authService: AuthService
    ): CalorieIntakeRepository {
        return CalorieIntakeRepositoryImpl(apiService, authService)
    }


    @Provides
    @Singleton
    fun provideUserProfileRepository(
        apiService: ApiService, authService: AuthService
    ): UserProfileRepository {
        return UserProfileRepositoryImpl(apiService, authService)
    }

}