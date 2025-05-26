package aut.bme.hu.fitness.di

import aut.bme.hu.fitness.api.UnauthenticatedApiService
import aut.bme.hu.fitness.api.AuthenticatedApiService
import aut.bme.hu.fitness.auth.AuthInterceptor
import aut.bme.hu.fitness.api.jsonAdapter.LocalDateAdapter
import aut.bme.hu.fitness.auth.service.AuthService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private var BASE_URL = "https://deliberate-guinevere-zsohajdu1-aa0139fc.koyeb.app/"
    
    @Provides
    @Singleton
    fun provideAuthInterceptor(authService: AuthService): AuthInterceptor {
        return AuthInterceptor(authService)
    }

    @Provides
    @Singleton
    @Named("authenticated")
    fun provideAuthenticatedOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("unauthenticated")
    fun provideUnauthenticatedOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    @Named("authenticated")
    fun provideAuthenticatedRetrofit(@Named("authenticated") okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("unauthenticated")
    fun provideUnauthenticatedRetrofit(@Named("unauthenticated") okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticatedApiService(@Named("authenticated") retrofit: Retrofit): AuthenticatedApiService {
        return retrofit.create(AuthenticatedApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUnauthenticatedApiService(@Named("unauthenticated") retrofit: Retrofit): UnauthenticatedApiService {
        return retrofit.create(UnauthenticatedApiService::class.java)
    }
}
