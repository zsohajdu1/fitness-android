package aut.bme.hu.fitness.domain.repository.impl

import aut.bme.hu.fitness.domain.api.ApiService
import aut.bme.hu.fitness.domain.model.UserProfile
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.service.AuthService
import retrofit2.HttpException
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val authService: AuthService
) : UserProfileRepository {

    override suspend fun getUserProfile(): UserProfile? {
        try {
            return api.getUserProfile(authService.currentUserId)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getUserProfileExists(): Boolean {
        try {

            return api.getUserProfileExists(authService.currentUserId)
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