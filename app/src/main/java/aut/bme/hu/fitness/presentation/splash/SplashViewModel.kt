package aut.bme.hu.fitness.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.bme.hu.fitness.AUTH_SCREEN
import aut.bme.hu.fitness.MAIN_SCREEN
import aut.bme.hu.fitness.auth.service.AuthService
import aut.bme.hu.fitness.auth.service.impl.AuthServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    fun onAppStart(openScreen: (String) -> Unit) {
        viewModelScope.launch {
            authService.initialize()
            if (authService.hasUser) openScreen(MAIN_SCREEN)
            else openScreen(AUTH_SCREEN)
        }
    }
}
