package aut.bme.hu.fitness.domain.repository

import aut.bme.hu.fitness.domain.model.Steps
import java.time.LocalDate

interface StepsRepository {
    suspend fun saveSteps(steps: Steps)

    suspend fun getDateSteps(date: LocalDate): Steps
}