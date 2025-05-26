package aut.bme.hu.fitness.domain.repository.impl

import aut.bme.hu.fitness.api.AuthenticatedApiService
import aut.bme.hu.fitness.domain.model.Exercise
import aut.bme.hu.fitness.domain.repository.ExerciseRepository
import java.time.LocalDate
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val api: AuthenticatedApiService
) : ExerciseRepository {

    override suspend fun getDateExercises(date: LocalDate): List<Exercise> {
        try {
            return api.getDateExercises(date)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createExercise(exercise: Exercise) {
        try {
            api.createExercise(exercise)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveExercise(exercise: Exercise) {
        try {
            api.saveExercise(exercise)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        try {
            exercise.id?.let { api.deleteExercise(it) }
        } catch (e: Exception) {
            throw e
        }
    }
}