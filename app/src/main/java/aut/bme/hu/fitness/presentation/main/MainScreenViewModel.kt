package aut.bme.hu.fitness.presentation.main

import androidx.lifecycle.ViewModel
import aut.bme.hu.fitness.CREATION_SCREEN
import aut.bme.hu.fitness.SPLASH_SCREEN
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {
    suspend fun refresh(navigateTo: (String) -> Unit) {
        if (!userProfileRepository.getUserProfileExists()) {
            navigateTo(CREATION_SCREEN)
        }
    }
}
