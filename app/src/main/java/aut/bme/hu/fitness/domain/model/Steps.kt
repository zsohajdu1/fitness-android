package aut.bme.hu.fitness.domain.model

import java.time.LocalDate

data class Steps(
    val id: Long?,
    val email: String?,
    val date: LocalDate,
    val count: Int
)