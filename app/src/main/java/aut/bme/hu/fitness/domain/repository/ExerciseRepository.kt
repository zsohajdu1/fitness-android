package aut.bme.hu.fitness.domain.repository

import aut.bme.hu.fitness.domain.model.Exercise
import java.time.LocalDate

interface ExerciseRepository {

    suspend fun createExercise(exercise: Exercise)

    suspend fun saveExercise(exercise: Exercise)

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun getDateExercises(date: LocalDate): List<Exercise>
}