package aut.bme.hu.fitness.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.bme.hu.fitness.common.ext.isValidEmail
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService, private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState = _uiState.asStateFlow()

    data class LoginUiData(
        val email: String, val password: String
    )

    sealed class LoginUiState {
        data object Loading : LoginUiState()
        data class Error(val message: String) : LoginUiState()
        data class Created(
            val data: LoginUiData
        ) : LoginUiState()

        data object SuccessWithProfile : LoginUiState()
        data object SuccessWithoutProfile : LoginUiState()
    }


    fun refresh() {
        _uiState.value = LoginUiState.Loading
        val data = LoginUiData(
            "", ""
        )
        _uiState.value = LoginUiState.Created(data)
    }

    fun onUsernameChanged(value: String) {
        _uiState.update {
            if (it is LoginUiState.Created) {
                it.copy(data = it.data.copy(email = value))
            } else {
                it
            }
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            if (it is LoginUiState.Created) {
                it.copy(data = it.data.copy(password = value))
            } else {
                it
            }
        }
    }

    fun onLoginClick() {
        val state = uiState.value as LoginUiState.Created
        if (!state.data.email.isValidEmail()) {
            _uiState.value = LoginUiState.Error("Enter a valid email")
        } else if (state.data.password.isBlank()) {
            _uiState.value = LoginUiState.Error("Enter a valid password")
        } else viewModelScope.launch {
            try {
                authService.authenticate(state.data.email, state.data.password)
                if (userProfileRepository.getUserProfileExists()) {
                    _uiState.value = LoginUiState.SuccessWithoutProfile
                } else {
                    _uiState.value = LoginUiState.SuccessWithProfile
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}