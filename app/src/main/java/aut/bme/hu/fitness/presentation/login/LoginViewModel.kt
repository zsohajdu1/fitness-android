package aut.bme.hu.fitness.presentation.login

import androidx.lifecycle.ViewModel
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.repository.UserRepository
import aut.bme.hu.fitness.presentation.home.HomeViewModel.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState = _uiState.asStateFlow()

    data class LoginUiData(
        val username: String,
        val password: String
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
            "",
            ""
        )
        _uiState.value = LoginUiState.Created(data)
    }

    fun onUsernameChanged(value: String) {
        _uiState.update {
            if (it is LoginUiState.Created) {
                it.copy(data = it.data.copy(username = value))
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
        TODO("Not yet implemented")
    }
}