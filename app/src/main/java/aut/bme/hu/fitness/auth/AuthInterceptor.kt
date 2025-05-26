package aut.bme.hu.fitness.auth

import aut.bme.hu.fitness.auth.service.AuthService
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

        val token = authService.getAccessToken()
        val modifiedRequest: Request =
            originalRequest.newBuilder().header("Authorization", "Bearer $token").build()

        return chain.proceed(modifiedRequest)
    }
}