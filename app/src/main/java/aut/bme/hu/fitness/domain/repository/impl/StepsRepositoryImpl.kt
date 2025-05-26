package aut.bme.hu.fitness.domain.repository.impl

import aut.bme.hu.fitness.api.AuthenticatedApiService
import aut.bme.hu.fitness.domain.model.Steps
import aut.bme.hu.fitness.domain.repository.StepsRepository
import java.time.LocalDate
import javax.inject.Inject

class StepsRepositoryImpl @Inject constructor(
    private val api: AuthenticatedApiService
) : StepsRepository {
    override suspend fun saveSteps(steps: Steps) {
        try {
            api.saveSteps(steps)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getDateSteps(date: LocalDate): Steps {
        try {
            return api.getDateSteps(date)
        } catch (e: Exception) {
            throw e
        }
    }
}