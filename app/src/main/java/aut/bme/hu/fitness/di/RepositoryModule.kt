package aut.bme.hu.fitness.di

import aut.bme.hu.fitness.domain.repository.CalorieIntakeRepository
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.repository.UserRepository
import aut.bme.hu.fitness.domain.repository.impl.CalorieIntakeRepositoryImpl
import aut.bme.hu.fitness.domain.repository.impl.UserProfileRepositoryImpl
import aut.bme.hu.fitness.domain.repository.impl.UserRepositoryImpl
import dagger.Binds
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
    fun provideCalorieIntakeRepository(): CalorieIntakeRepository {
        return CalorieIntakeRepositoryImpl()
    }


    @Provides
    @Singleton
    fun provideUserProfileRepository(): UserProfileRepository {
        return UserProfileRepositoryImpl()
    }


    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl()
    }
}