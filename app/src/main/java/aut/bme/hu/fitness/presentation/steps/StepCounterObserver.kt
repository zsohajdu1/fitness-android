package aut.bme.hu.fitness.presentation.steps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun StepCounterObserver(
    viewModel: StepCounterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val manager = remember {
        StepCounterManager(context) {
            viewModel.updateSteps(it)
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> manager.startListening()
                Lifecycle.Event.ON_PAUSE -> manager.stopListening()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            manager.stopListening()
            lifecycleOwner.lifecycle.removeObserver(observer)
            viewModel.persistSteps()
        }
    }
}
