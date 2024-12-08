package aut.bme.hu.fitness.domain.api

import aut.bme.hu.fitness.domain.service.AuthService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val authService: AuthService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        if (!authService.hasUser) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { authService.getAuthToken() }
        val modifiedRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(modifiedRequest)
    }
}