package aut.bme.hu.fitness.api

import aut.bme.hu.fitness.auth.request.LoginRequest
import aut.bme.hu.fitness.auth.request.RegisterRequest
import aut.bme.hu.fitness.auth.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UnauthenticatedApiService {

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest) : AuthResponse

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("/api/auth/validate")
    suspend fun validate(@Query("token") request: String): Boolean

}