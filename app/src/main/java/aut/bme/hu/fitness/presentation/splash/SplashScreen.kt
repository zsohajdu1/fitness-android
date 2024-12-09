package aut.bme.hu.fitness.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    navigateTo: (String) -> Unit, viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        viewModel.onAppStart(navigateTo)
    }
}