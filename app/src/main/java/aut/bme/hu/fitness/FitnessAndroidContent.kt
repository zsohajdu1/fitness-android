package aut.bme.hu.fitness

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import aut.bme.hu.fitness.presentation.auth.AuthScreen
import aut.bme.hu.fitness.presentation.creation.CreationScreen
import aut.bme.hu.fitness.presentation.login.LoginScreen
import aut.bme.hu.fitness.presentation.main.MainScreen
import aut.bme.hu.fitness.presentation.register.RegisterScreen
import aut.bme.hu.fitness.presentation.splash.SplashScreen

@Composable
fun FitnessAndroidContent() {
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavGraph(navController)
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = SPLASH_SCREEN
    ) {
        composable(SPLASH_SCREEN) {
            SplashScreen({ route: String ->
                navController.navigate(route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        composable(REGISTER_SCREEN) {
            RegisterScreen({
                navController.navigate(CREATION_SCREEN) {}
            })
        }
        composable(LOGIN_SCREEN) {
            LoginScreen({ route: String ->
                navController.navigate(route) {}
            })
        }
        composable(MAIN_SCREEN) {
            MainScreen({ route: String ->
                navController.navigate(route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        composable(CREATION_SCREEN) {
            CreationScreen({
                navController.navigate(MAIN_SCREEN) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        composable(AUTH_SCREEN) {
            AuthScreen({ route: String ->
                navController.navigate(route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
    }
}