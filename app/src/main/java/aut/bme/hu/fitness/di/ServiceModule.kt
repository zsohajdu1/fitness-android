package aut.bme.hu.fitness.di

import aut.bme.hu.fitness.domain.service.AuthService
import aut.bme.hu.fitness.domain.service.impl.AuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindAuthService(
        authServiceImpl: AuthServiceImpl
    ): AuthService

}