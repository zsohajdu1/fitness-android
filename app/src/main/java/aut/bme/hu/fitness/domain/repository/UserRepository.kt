package aut.bme.hu.fitness.domain.repository


interface UserRepository {

    suspend fun registerUser(username: String, password: String)

    suspend fun loginUser(username: String, password: String)

    suspend fun logoutUser()
}