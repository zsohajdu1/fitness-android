package aut.bme.hu.fitness.domain.model

import java.time.LocalDate

data class CalorieIntake(
    val id: Long?,
    val uid: String,
    val date: LocalDate,
    val name: String,
    val calories: Double,
    val quantity: Int
)
