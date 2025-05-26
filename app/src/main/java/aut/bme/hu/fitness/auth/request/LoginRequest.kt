package aut.bme.hu.fitness.auth.request

data class LoginRequest (
    val email: String,
    val password: String
)