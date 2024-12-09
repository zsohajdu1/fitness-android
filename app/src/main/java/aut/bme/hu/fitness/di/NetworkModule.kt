package aut.bme.hu.fitness.di

import aut.bme.hu.fitness.domain.api.ApiService
import aut.bme.hu.fitness.domain.api.AuthInterceptor
import aut.bme.hu.fitness.domain.api.jsonAdapter.LocalDateAdapter
import aut.bme.hu.fitness.domain.service.AuthService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(authService: AuthService): AuthInterceptor {
        return AuthInterceptor(authService)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson =
            GsonBuilder().registerTypeAdapter(LocalDate::class.java, LocalDateAdapter()).create()

        return Retrofit.Builder().baseUrl("https://fitness-latest.onrender.com/")
            .client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}