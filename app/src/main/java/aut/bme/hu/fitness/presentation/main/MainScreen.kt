package aut.bme.hu.fitness.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import aut.bme.hu.fitness.SPLASH_SCREEN
import aut.bme.hu.fitness.presentation.home.HomeScreen
import aut.bme.hu.fitness.presentation.journal.JournalScreen
import aut.bme.hu.fitness.presentation.profile.ProfileScreen

@Composable
fun MainScreen(
    navigateTo: (String) -> Unit, viewModel: MainScreenViewModel = hiltViewModel()
) {
    val navItemList = listOf(
        NavItem("Journal", Icons.Default.DateRange),
        NavItem("Home", Icons.Default.Home),
        NavItem("Profile", Icons.Default.AccountCircle)
    )

    LaunchedEffect(true) {
        viewModel.refresh(navigateTo)
    }

    var selectedIndex by remember {
        mutableIntStateOf(1)
    }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        NavigationBar {
            navItemList.forEachIndexed { index, navItem ->
                NavigationBarItem(selected = selectedIndex == index, onClick = {
                    selectedIndex = index
                }, icon = {
                    Icon(
                        imageVector = navItem.icon, contentDescription = navItem.label
                    )
                }, label = {
                    Text(text = navItem.label)
                })
            }
        }
    }) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding), selectedIndex = selectedIndex
        ) { navigateTo(SPLASH_SCREEN) }
    }
}


data class NavItem(
    val label: String, val icon: ImageVector
)

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int, logOut: () -> Unit) {
    when (selectedIndex) {
        0 -> JournalScreen()
        1 -> HomeScreen()
        2 -> ProfileScreen(logOut)
    }
}