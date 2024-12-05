package aut.bme.hu.fitness.domain.model

import aut.bme.hu.fitness.domain.model.enums.ActivityLevel
import aut.bme.hu.fitness.domain.model.enums.Gender
import java.time.LocalDate

data class UserProfile(
    val id: Long?,
    val userId: Long,
    val birthDate: LocalDate,
    val gender: Gender,
    val height: Int,
    val weight: Int,
    val activityLevel: ActivityLevel,
    val tdee: Double
)
