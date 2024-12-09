package aut.bme.hu.fitness.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.bme.hu.fitness.domain.model.CalorieIntake
import aut.bme.hu.fitness.domain.model.UserProfile
import aut.bme.hu.fitness.domain.repository.CalorieIntakeRepository
import aut.bme.hu.fitness.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val calorieIntakeRepository: CalorieIntakeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    data class HomeUiData(
        val userProfile: UserProfile,
        val calorieIntakes: List<CalorieIntake>,
        val creatingCalorieIntake: Boolean,
        val creatingCalorieIntakeName: String,
        val creatingCalorieIntakeCalories: Double,
        val creatingCalorieIntakeQuantity: Int
    )

    sealed class HomeUiState {
        data object Loading : HomeUiState()
        data class Error(val message: String) : HomeUiState()
        data class Success(
            val data: HomeUiData
        ) : HomeUiState()
    }

    fun refresh() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val userProfile = userProfileRepository.getUserProfile()
                val calorieIntakes = calorieIntakeRepository.getDateCalorieIntakes(LocalDate.now())
                val data = userProfile?.let {
                    HomeUiData(
                        it,
                        calorieIntakes,
                        false,
                        "",
                        0.0,
                        0
                    )
                }
                _uiState.value = data?.let { HomeUiState.Success(it) }!!
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }


    fun onCreatingCalorieIntakeChanged(value: Boolean) {
        _uiState.update {
            if (it is HomeUiState.Success) {
                it.copy(data = it.data.copy(creatingCalorieIntake = value))
            } else {
                it
            }
        }
    }

    fun onCreatingCalorieIntakeNameChanged(value: String) {
        _uiState.update {
            if (it is HomeUiState.Success) {
                it.copy(data = it.data.copy(creatingCalorieIntakeName = value))
            } else {
                it
            }
        }
    }

    fun onCreatingCalorieIntakeCaloriesChanged(value: Double) {
        _uiState.update {
            if (it is HomeUiState.Success) {
                it.copy(data = it.data.copy(creatingCalorieIntakeCalories = value))
            } else {
                it
            }
        }
    }

    fun onCreatingCalorieIntakeQuantityChanged(value: Int) {
        _uiState.update {
            if (it is HomeUiState.Success) {
                it.copy(data = it.data.copy(creatingCalorieIntakeQuantity = value))
            } else {
                it
            }
        }
    }

    fun onCreatingCalorieIntake() {
        viewModelScope.launch {
            try {
                if (_uiState.value is HomeUiState.Success) {
                    val calorieIntake =
                        (_uiState.value as HomeUiState.Success).data.userProfile.uid?.let {
                            CalorieIntake(
                                id = null,
                                uid = it,
                                date = LocalDate.now(),
                                name = (_uiState.value as HomeUiState.Success).data.creatingCalorieIntakeName,
                                calories = (_uiState.value as HomeUiState.Success).data.creatingCalorieIntakeCalories,
                                quantity = (_uiState.value as HomeUiState.Success).data.creatingCalorieIntakeQuantity
                            )
                        }
                    if (calorieIntake != null) {
                        calorieIntakeRepository.createCalorieIntake(calorieIntake)
                    }
                    onCreatingCalorieIntakeChanged(false)
                    refresh()
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}