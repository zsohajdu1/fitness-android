package aut.bme.hu.fitness.domain.repository

import aut.bme.hu.fitness.domain.model.CalorieIntake
import java.time.LocalDate

interface CalorieIntakeRepository {

    suspend fun createCalorieIntake(calorieIntake: CalorieIntake)

    suspend fun saveCalorieIntake(calorieIntake: CalorieIntake)

    suspend fun deleteCalorieIntake(calorieIntake: CalorieIntake)

    suspend fun getDateCalorieIntakes(date: LocalDate): List<CalorieIntake>
}