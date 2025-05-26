package aut.bme.hu.fitness.di

import android.content.Context
import aut.bme.hu.fitness.api.UnauthenticatedApiService
import aut.bme.hu.fitness.auth.service.AuthService
import aut.bme.hu.fitness.auth.service.impl.AuthServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(
        unauthenticatedApiService: UnauthenticatedApiService,
        @ApplicationContext context: Context
    ): AuthService {
        return AuthServiceImpl(unauthenticatedApiService, context)
    }

}