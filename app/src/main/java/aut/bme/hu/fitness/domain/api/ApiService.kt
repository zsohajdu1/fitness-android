package aut.bme.hu.fitness.domain.api

import aut.bme.hu.fitness.domain.model.CalorieIntake
import aut.bme.hu.fitness.domain.model.UserProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import java.time.LocalDate

interface ApiService {

    @GET("/api/userprofile")
    suspend fun getUserProfile(): UserProfile

    @PUT("/api/userprofile")
    suspend fun saveUserProfile(@Body request: UserProfile)

    @POST("/api/userprofile")
    suspend fun createUserProfile(@Body request: UserProfile)

    @GET("/api/calorieintakes/date")
    suspend fun getDateCalorieIntakes(@Body request: LocalDate): List<CalorieIntake>

    @POST("/api/calorieintakes")
    suspend fun createCalorieIntake(@Body request: CalorieIntake)

    @PUT("/api/calorieintakes")
    suspend fun saveCalorieIntake(@Body request: CalorieIntake)

    @DELETE("/api/calorieintakes")
    suspend fun deleteCalorieIntake(@Body request: CalorieIntake)

    @POST("/auth/register")
    suspend fun registerUser(@Body request: UserRequest)

    @POST("/auth/login")
    suspend fun loginUser(@Body request: UserRequest)

    @POST("/auth/logout")
    suspend fun logoutUser()
}

data class UserRequest(val username: String, val password: String)