package aut.bme.hu.fitness.domain.model

import java.time.LocalDate

data class CalorieIntake(
    val id: Long?,
    val userId: Long,
    val date: LocalDate,
    val name: String,
    val calories: Double,
    val quantity: Int
)
