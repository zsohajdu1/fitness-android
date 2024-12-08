package aut.bme.hu.fitness.domain.repository

import aut.bme.hu.fitness.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(): UserProfile?

    suspend fun getUserProfileExists(): Boolean

    suspend fun saveUserProfile(userProfile: UserProfile)

    suspend fun createUserProfile(userProfile: UserProfile)
}