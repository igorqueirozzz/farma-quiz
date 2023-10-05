package dev.queiroz.farmaquiz.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.queiroz.farmaquiz.ui.components.QuizAppBar
import dev.queiroz.farmaquiz.ui.components.QuizNavHost
import dev.queiroz.farmaquiz.ui.theme.DoseDeConhecimentoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizApp() {
    DoseDeConhecimentoTheme {
        val navHostController = rememberNavController()
        val navBackStack by navHostController.currentBackStackEntryAsState()
        val allScreens = remember { allTabScreens }
        val currentScreen = allScreens.find { it.route == navBackStack?.destination?.route } ?: Home

        var showAppBar by remember { mutableStateOf(true) }

        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    AnimatedVisibility(visible = showAppBar) {
                        QuizAppBar(
                            allScreens = allScreens,
                            currentScreen = currentScreen,
                            onTabClick = {
                                navHostController.navigateSingleTop(route = it.route)
                            }
                        )
                    }
                }
            ) { innerPadding ->
                QuizNavHost(
                    modifier = Modifier.padding(innerPadding),
                    navController = navHostController,
                    onRequestChangeAppBar = { showAppBar = it }
                )
            }
        }
    }
}

fun NavHostController.navigateSingleTop(route: String) {
    this.navigate(route = route) {
        launchSingleTop = true
    }
}