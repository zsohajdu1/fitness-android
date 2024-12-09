package aut.bme.hu.fitness.presentation.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.bme.hu.fitness.domain.model.CalorieIntake
import aut.bme.hu.fitness.domain.repository.CalorieIntakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val calorieIntakeRepository: CalorieIntakeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<JournalUiState>(JournalUiState.Loading)
    val uiState = _uiState.asStateFlow()

    data class JournalUiData(
        val calorieIntakes: List<CalorieIntake>, val date: LocalDate = LocalDate.now()
    )

    sealed class JournalUiState {
        data object Loading : JournalUiState()
        data class Error(val message: String) : JournalUiState()
        data class Success(
            val data: JournalUiData
        ) : JournalUiState()
    }

    fun refresh(date: LocalDate = LocalDate.now()) {
        _uiState.value = JournalUiState.Loading
        viewModelScope.launch {
            try {
                val calorieIntakes = calorieIntakeRepository.getDateCalorieIntakes(date)
                val data = JournalUiData(
                    calorieIntakes, date
                )
                _uiState.value = JournalUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = JournalUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun onDateChanged(value: LocalDate) {
        if (value <= LocalDate.now()) {
            _uiState.update {
                if (it is JournalUiState.Success) {
                    it.copy(data = it.data.copy(date = value))
                } else {
                    it
                }
            }
            refresh(value)
        }
    }

    fun onNextDay() {
        val date = (_uiState.value as JournalUiState.Success).data.date.plusDays(1)
        onDateChanged(date)
    }

    fun onPreviousDay() {
        val date = (_uiState.value as JournalUiState.Success).data.date.minusDays(1)
        onDateChanged(date)
    }
}