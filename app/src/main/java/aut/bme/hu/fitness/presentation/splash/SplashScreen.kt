package aut.bme.hu.fitness.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import aut.bme.hu.fitness.presentation.register.RegisterViewModel

@Composable
fun SplashScreen(
    navigateTo: (String) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        viewModel.onAppStart(navigateTo)
    }
}