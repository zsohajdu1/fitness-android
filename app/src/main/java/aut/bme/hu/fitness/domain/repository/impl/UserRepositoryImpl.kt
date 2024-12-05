package aut.bme.hu.fitness.domain.repository.impl

import aut.bme.hu.fitness.domain.api.RetrofitInstance
import aut.bme.hu.fitness.domain.api.UserRequest
import aut.bme.hu.fitness.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val api = RetrofitInstance.api

    override suspend fun registerUser(username: String, password: String) {
        api.registerUser(UserRequest(username, password))
    }

    override suspend fun loginUser(username: String, password: String) {
        api.loginUser(UserRequest(username, password))
    }

    override suspend fun logoutUser() {
        api.logoutUser()
    }
}