package aut.bme.hu.fitness.domain.repository.impl

import aut.bme.hu.fitness.domain.api.RetrofitInstance
import aut.bme.hu.fitness.domain.model.UserProfile
import aut.bme.hu.fitness.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl : UserProfileRepository {
    private val api = RetrofitInstance.api

    override suspend fun getUserProfile(): UserProfile {
        return api.getUserProfile()
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        api.saveUserProfile(userProfile)
    }

    override suspend fun createUserProfile(userProfile: UserProfile) {
        api.createUserProfile(userProfile)
    }
}