package aut.bme.hu.fitness.domain.api

import aut.bme.hu.fitness.domain.model.CalorieIntake
import aut.bme.hu.fitness.domain.model.UserProfile
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.time.LocalDate

interface ApiService {

    @GET("/api/userprofile")
    suspend fun getUserProfile(@Query("uid") uid: String): UserProfile?

    @GET("/api/userprofile/exists")
    suspend fun getUserProfileExists(@Query("uid") uid: String): Boolean

    @PUT("/api/userprofile")
    suspend fun saveUserProfile(@Body request: UserProfile)

    @POST("/api/userprofile")
    suspend fun createUserProfile(@Body request: UserProfile)

    @GET("/api/calorieintakes/date")
    suspend fun getDateCalorieIntakes(
        @Query("date") request: LocalDate, @Query("uid") uid: String
    ): List<CalorieIntake>

    @POST("/api/calorieintakes")
    suspend fun createCalorieIntake(@Body request: CalorieIntake)

    @PUT("/api/calorieintakes")
    suspend fun saveCalorieIntake(@Body request: CalorieIntake)

    @DELETE("/api/calorieintakes")
    suspend fun deleteCalorieIntake(@Query("id") request: Long)

}