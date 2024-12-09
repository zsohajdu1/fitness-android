package aut.bme.hu.fitness.presentation.splash

import androidx.lifecycle.ViewModel
import aut.bme.hu.fitness.AUTH_SCREEN
import aut.bme.hu.fitness.MAIN_SCREEN
import aut.bme.hu.fitness.domain.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authService: AuthService,
) : ViewModel() {
    fun onAppStart(openScreen: (String) -> Unit) {
        if (authService.hasUser) openScreen(MAIN_SCREEN)
        else openScreen(AUTH_SCREEN)
    }
}