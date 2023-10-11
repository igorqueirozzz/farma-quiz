package dev.queiroz.farmaquiz.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.model.ThemeMode
import dev.queiroz.farmaquiz.ui.components.QuizAppBar
import dev.queiroz.farmaquiz.ui.components.QuizNavHost
import dev.queiroz.farmaquiz.ui.screen.home.HomeViewModel
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizGameViewModel
import dev.queiroz.farmaquiz.ui.screen.welcome.WelcomeScreen
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizApp() {

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val quizGameViewModel = hiltViewModel<QuizGameViewModel>()

    val userPreferences by homeViewModel.userPreferences.observeAsState()

    if (userPreferences == null) {
        FarmaQuizTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.farma_quiz_logo),
                    contentDescription = null
                )
            }
        }
    } else {
        FarmaQuizTheme(
            darkTheme = if (userPreferences!!.themeMode == ThemeMode.AUTO) isSystemInDarkTheme() else userPreferences!!.themeMode == ThemeMode.DARK
        ) {
            val navHostController = rememberNavController()
            val navBackStack by navHostController.currentBackStackEntryAsState()
            val allScreens = remember { allTabScreens }
            val currentScreen =
                allScreens.find { it.route == navBackStack?.destination?.route } ?: Home
            var showAppBar by remember { mutableStateOf(true) }

            if (userPreferences?.isFirstLaunch == true) {
                WelcomeScreen(
                    onUserPassWelcomeScreen = homeViewModel::onFinishWelcomeScreen
                )
            } else {
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
                            onRequestChangeAppBar = { showAppBar = it },
                            quizGameViewModel = quizGameViewModel,
                            homeViewModel = homeViewModel
                        )
                    }
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTop(route: String) {
    this.navigate(route = route) {
        launchSingleTop = true
    }
}
fun NavHostController.navigateSingleTopWithStringArg(route: String, arg: String) {
    this.navigate(route = "$route/$arg") {
        launchSingleTop = true
    }
}
