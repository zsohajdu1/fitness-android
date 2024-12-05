package aut.bme.hu.fitness.presentation.register

import androidx.lifecycle.ViewModel
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Loading)
    val uiState = _uiState.asStateFlow()

    data class RegisterUiData(
        val username: String,
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

    fun onUsernameChanged(value: String) {
        _uiState.update {
            if (it is RegisterUiState.Created) {
                it.copy(data = it.data.copy(username = value))
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
        TODO("Not yet implemented")
        //_uiState.value = RegisterUiState.Success
    }
}