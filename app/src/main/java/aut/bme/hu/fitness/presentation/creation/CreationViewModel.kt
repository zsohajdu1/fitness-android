package aut.bme.hu.fitness.presentation.creation

import android.util.Log
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.bme.hu.fitness.common.ext.isValidEmail
import aut.bme.hu.fitness.domain.model.UserProfile
import aut.bme.hu.fitness.domain.model.enums.ActivityLevel
import aut.bme.hu.fitness.domain.model.enums.Gender
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.service.AuthService
import aut.bme.hu.fitness.presentation.register.RegisterViewModel.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class CreationViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val authService: AuthService
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreationUiState>(CreationUiState.Loading)
    val uiState = _uiState.asStateFlow()

    data class CreationUiData constructor(
        val height: Int,
        val weight: Int,
        val birthDate: LocalDate,
        val gender: Gender,
        val activityLevel: ActivityLevel,
        val changingActivityLevel: Boolean = false,
        val changingGender: Boolean = false,
        val changingBirthDate: Boolean = false,
    )

    sealed class CreationUiState {
        data object Loading : CreationUiState()
        data object Created : CreationUiState()
        data class Error(val message: String) : CreationUiState()
        data class Success(
            val data: CreationUiData
        ) : CreationUiState()
    }

    fun refresh() {
        _uiState.value = CreationUiState.Loading
        val data = CreationUiData(
            0,
            0,
            LocalDate.now(),
            Gender.Male,
            ActivityLevel.Sedentary
        )
        _uiState.value = CreationUiState.Success(data)
    }

    fun onHeightChanged(value: Int) {
        _uiState.update {
            if (it is CreationUiState.Success) {
                it.copy(data = it.data.copy(height = value))
            } else {
                it
            }
        }
    }

    fun onWeightChanged(value: Int) {
        _uiState.update {
            if (it is CreationUiState.Success) {
                it.copy(data = it.data.copy(weight = value))
            } else {
                it
            }
        }
    }

    fun onBirthDateChanged(value: Long?) {
        if (value == null) return
        val date = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate();
        _uiState.update {
            if (it is CreationUiState.Success) {
                it.copy(data = it.data.copy(birthDate = date))
            } else {
                it
            }
        }
    }

    fun onGenderChanged(value: Gender) {
        _uiState.update {
            if (it is CreationUiState.Success) {
                it.copy(data = it.data.copy(gender = value))
            } else {
                it
            }
        }
    }

    fun onActivityLevelChanged(value: ActivityLevel) {
        _uiState.update {
            if (it is CreationUiState.Success) {
                it.copy(data = it.data.copy(activityLevel = value))
            } else {
                it
            }
        }
    }

    fun onChangingActivityLevelChanged(value: Boolean) {
        _uiState.update {
            if (it is CreationUiState.Success) {
                it.copy(data = it.data.copy(changingActivityLevel = value))
            } else {
                it
            }
        }
    }

    fun onChangingGenderChanged(value: Boolean) {
        _uiState.update {
            if (it is CreationUiState.Success) {
                it.copy(data = it.data.copy(changingGender = value))
            } else {
                it
            }
        }
    }

    fun onChangingBirthDateChanged(value: Boolean) {
        _uiState.update {
            if (it is CreationUiState.Success) {
                it.copy(data = it.data.copy(changingBirthDate = value))
            } else {
                it
            }

        }

    }

    fun onCreateProfile() {
        val state = uiState.value as CreationUiState.Success
        if (state.data.birthDate > LocalDate.now()) {
            _uiState.value = CreationUiState.Error("Enter a valid birth date")
        } else if (state.data.height < 0) {
            _uiState.value = CreationUiState.Error("Enter a valid height")
        } else if (state.data.weight < 0) {
            _uiState.value = CreationUiState.Error("Enter a valid weight")
        } else viewModelScope.launch {
            try {
                _uiState.value = CreationUiState.Loading
                val userProfile = UserProfile(
                    height = state.data.height,
                    weight = state.data.weight,
                    birthDate = state.data.birthDate,
                    gender = state.data.gender,
                    activityLevel = state.data.activityLevel,
                    uid = authService.currentUserId,
                    tdee = 0.0,
                    id = null
                )
                userProfileRepository.createUserProfile(userProfile)
                _uiState.value = CreationUiState.Created
            } catch (e: Exception) {
                _uiState.value = CreationUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}