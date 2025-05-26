package aut.bme.hu.fitness.api

import aut.bme.hu.fitness.domain.model.CalorieIntake
import aut.bme.hu.fitness.domain.model.UserProfile
import aut.bme.hu.fitness.domain.model.Exercise
import aut.bme.hu.fitness.domain.model.Steps
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.time.LocalDate

interface AuthenticatedApiService {

    @GET("/api/userprofile")
    suspend fun getUserProfile(): UserProfile?

    @GET("/api/userprofile/exists")
    suspend fun getUserProfileExists(): Boolean

    @PUT("/api/userprofile")
    suspend fun saveUserProfile(@Body request: UserProfile)

    @POST("/api/userprofile")
    suspend fun createUserProfile(@Body request: UserProfile)

    @GET("/api/calorieintakes/date")
    suspend fun getDateCalorieIntakes(@Query("date") request: LocalDate): List<CalorieIntake>

    @POST("/api/calorieintakes")
    suspend fun createCalorieIntake(@Body request: CalorieIntake)

    @PUT("/api/calorieintakes")
    suspend fun saveCalorieIntake(@Body request: CalorieIntake)

    @DELETE("/api/calorieintakes")
    suspend fun deleteCalorieIntake(@Query("id") request: Long)

    @GET("/api/exercises/date")
    suspend fun getDateExercises(@Query("date") request: LocalDate): List<Exercise>

    @POST("/api/exercises")
    suspend fun createExercise(@Body request: Exercise)

    @PUT("/api/exercises")
    suspend fun saveExercise(@Body request: Exercise)

    @DELETE("/api/exercises")
    suspend fun deleteExercise(@Query("id") request: Long)

    @GET("/api/steps/date")
    suspend fun getDateSteps(@Query("date") request: LocalDate): Steps

    @PUT("/api/steps")
    suspend fun saveSteps(@Body request: Steps)


}