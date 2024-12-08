package aut.bme.hu.fitness.presentation.register

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
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Loading)
    val uiState = _uiState.asStateFlow()

    data class RegisterUiData(
        val email: String,
        val password: String,
        val repeatPassword: String
    )

    sealed class RegisterUiState {
        data object Loading : RegisterUiState()
        data class Error(val message: String) : RegisterUiState()
        data class Created(
            val data: RegisterUiData
        ) : RegisterUiState()

        data object Success : RegisterUiState()
    }


    fun refresh() {
        _uiState.value = RegisterUiState.Loading
        val data = RegisterUiData(
            "",
            "",
            ""
        )
        _uiState.value = RegisterUiState.Created(data)
    }

    fun onEmailChanged(value: String) {
        _uiState.update {
            if (it is RegisterUiState.Created) {
                it.copy(data = it.data.copy(email = value))
            } else {
                it
            }
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            if (it is RegisterUiState.Created) {
                it.copy(data = it.data.copy(password = value))
            } else {
                it
            }
        }
    }

    fun onRepeatPasswordChanged(value: String) {
        _uiState.update {
            if (it is RegisterUiState.Created) {
                it.copy(data = it.data.copy(repeatPassword = value))
            } else {
                it
            }
        }

    }

    fun onRegisterClick() {
        val state = uiState.value as RegisterUiState.Created
        if (!state.data.email.isValidEmail()) {
            _uiState.value = RegisterUiState.Error("Enter a valid email")
        } else if (state.data.password.isBlank()) {
            _uiState.value = RegisterUiState.Error("Enter a valid password")
        } else if (state.data.password != state.data.repeatPassword) {
            _uiState.value = RegisterUiState.Error("Passwords do not match")
        } else viewModelScope.launch {
            try {
                authService.signUp(state.data.email, state.data.password)
                _uiState.value = RegisterUiState.Success
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}