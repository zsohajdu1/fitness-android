package aut.bme.hu.fitness.domain.repository.impl

import aut.bme.hu.fitness.domain.api.RetrofitInstance
import aut.bme.hu.fitness.domain.model.CalorieIntake
import aut.bme.hu.fitness.domain.repository.CalorieIntakeRepository
import java.time.LocalDate

class CalorieIntakeRepositoryImpl : CalorieIntakeRepository {
    private val api = RetrofitInstance.api

    override suspend fun getDateCalorieIntakes(date: LocalDate): List<CalorieIntake> {
        return api.getDateCalorieIntakes(date)
    }

    override suspend fun createCalorieIntake(calorieIntake: CalorieIntake) {
        api.createCalorieIntake(calorieIntake)
    }

    override suspend fun saveCalorieIntake(calorieIntake: CalorieIntake) {
        api.saveCalorieIntake(calorieIntake)
    }

    override suspend fun deleteCalorieIntake(calorieIntake: CalorieIntake) {
        api.deleteCalorieIntake(calorieIntake)
    }
}