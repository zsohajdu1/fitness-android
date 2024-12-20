package aut.bme.hu.fitness.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.bme.hu.fitness.domain.model.UserProfile
import aut.bme.hu.fitness.domain.model.enums.ActivityLevel
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import aut.bme.hu.fitness.domain.service.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository, private val authService: AuthService
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    data class ProfileUiData(
        val userProfile: UserProfile,
        val newHeight: Int,
        val newWeight: Int,
        val newActivityLevel: ActivityLevel,
        val changingActivityLevel: Boolean = false
    )

    sealed class ProfileUiState {
        data object Loading : ProfileUiState()
        data class Error(val message: String) : ProfileUiState()
        data class Success(
            val data: ProfileUiData
        ) : ProfileUiState()
    }

    fun refresh() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                val userProfile = userProfileRepository.getUserProfile()
                val data = userProfile?.let {
                    ProfileUiData(
                        it, userProfile.height, userProfile.weight, userProfile.activityLevel
                    )
                }
                _uiState.value = data?.let { ProfileUiState.Success(it) }!!
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onNewActivityLevelChanged(value: ActivityLevel) {
        _uiState.update {
            if (it is ProfileUiState.Success) {
                it.copy(data = it.data.copy(newActivityLevel = value))
            } else {
                it
            }
        }
    }

    fun onNewHeightChanged(value: Int) {
        _uiState.update {
            if (it is ProfileUiState.Success) {
                it.copy(data = it.data.copy(newHeight = value))
            } else {
                it
            }
        }
    }

    fun onNewWeightChanged(value: Int) {
        _uiState.update {
            if (it is ProfileUiState.Success) {
                it.copy(data = it.data.copy(newWeight = value))
            } else {
                it
            }
        }
    }

    fun onChangingActivityLevelChanged(value: Boolean) {
        _uiState.update {
            if (it is ProfileUiState.Success) {
                it.copy(data = it.data.copy(changingActivityLevel = value))
            } else {
                it
            }
        }
    }

    fun onUpdateProfile() {
        viewModelScope.launch {
            try {
                if (_uiState.value is ProfileUiState.Success) {
                    val userProfile = (_uiState.value as ProfileUiState.Success).data.userProfile
                    val newUserProfile = UserProfile(
                        id = userProfile.id,
                        uid = userProfile.uid,
                        birthDate = userProfile.birthDate,
                        gender = userProfile.gender,
                        height = (_uiState.value as ProfileUiState.Success).data.newHeight,
                        weight = (_uiState.value as ProfileUiState.Success).data.newWeight,
                        activityLevel = (_uiState.value as ProfileUiState.Success).data.newActivityLevel,
                        tdee = userProfile.tdee
                    )
                    _uiState.value = ProfileUiState.Loading
                    userProfileRepository.saveUserProfile(newUserProfile)
                    refresh()
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onLogOut() {
        try {
            viewModelScope.launch {
                authService.signOut()
            }
        } catch (e: Exception) {
            _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
        }
    }
}
