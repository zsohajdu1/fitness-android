package aut.bme.hu.fitness.domain.repository.impl

import aut.bme.hu.fitness.api.AuthenticatedApiService
import aut.bme.hu.fitness.domain.model.UserProfile
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val api: AuthenticatedApiService
) : UserProfileRepository {

    override suspend fun getUserProfile(): UserProfile? {
        try {
            return api.getUserProfile()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUserProfileExists(): Boolean {
        try {

            return api.getUserProfileExists()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        try {
            api.saveUserProfile(userProfile)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun createUserProfile(userProfile: UserProfile) {
        try {
            api.createUserProfile(userProfile)
        } catch (e: Exception) {
            throw e
        }
    }
}