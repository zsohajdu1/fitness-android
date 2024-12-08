package aut.bme.hu.fitness.domain.repository.impl

import aut.bme.hu.fitness.domain.api.ApiService
import aut.bme.hu.fitness.domain.model.CalorieIntake
import aut.bme.hu.fitness.domain.repository.CalorieIntakeRepository
import aut.bme.hu.fitness.domain.service.AuthService
import java.time.LocalDate
import javax.inject.Inject

class CalorieIntakeRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val authService: AuthService
) : CalorieIntakeRepository {

    override suspend fun getDateCalorieIntakes(date: LocalDate): List<CalorieIntake> {
        try {
            return api.getDateCalorieIntakes(date, authService.currentUserId)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createCalorieIntake(calorieIntake: CalorieIntake) {
        try {
            api.createCalorieIntake(calorieIntake)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveCalorieIntake(calorieIntake: CalorieIntake) {
        try {
            api.saveCalorieIntake(calorieIntake)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteCalorieIntake(calorieIntake: CalorieIntake) {
        try {
            calorieIntake.id?.let { api.deleteCalorieIntake(it) }
        } catch (e: Exception) {
            throw e
        }
    }
}