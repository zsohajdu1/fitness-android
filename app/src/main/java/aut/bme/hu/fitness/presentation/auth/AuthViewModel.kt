package aut.bme.hu.fitness.presentation.auth

import androidx.lifecycle.ViewModel
import aut.bme.hu.fitness.LOGIN_SCREEN
import aut.bme.hu.fitness.REGISTER_SCREEN

class AuthViewModel : ViewModel() {

    fun onRegisterClick(openScreen: (String) -> Unit) {
        openScreen(REGISTER_SCREEN)
    }

    fun onLoginClick(openScreen: (String) -> Unit) {
        openScreen(LOGIN_SCREEN)
    }
}