package aut.bme.hu.fitness.presentation.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aut.bme.hu.fitness.domain.model.Steps
import aut.bme.hu.fitness.domain.repository.StepsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StepCounterViewModel @Inject constructor(
    private val stepsRepository: StepsRepository
) : ViewModel() {

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    fun updateSteps(count: Int) {
        _steps.value = count
    }

    fun persistSteps() {
        viewModelScope.launch {
            val stepsEntity = stepsRepository.getDateSteps(LocalDate.now())
            stepsRepository.saveSteps(
                Steps(
                    id = stepsEntity.id,
                    email = stepsEntity.email,
                    date = stepsEntity.date,
                    count = _steps.value
                )
            )
        }
    }
}
