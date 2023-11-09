package dev.queiroz.farmaquiz.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.model.ThemeMode
import dev.queiroz.farmaquiz.model.UserPreferences
import dev.queiroz.farmaquiz.ui.components.ErrorScreen
import dev.queiroz.farmaquiz.ui.components.QuizAppBar
import dev.queiroz.farmaquiz.ui.components.QuizNavHost
import dev.queiroz.farmaquiz.ui.screen.viewmodel.MainViewModel
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizGameViewModel
import dev.queiroz.farmaquiz.ui.screen.welcome.WelcomeScreen
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import dev.queiroz.farmaquiz.utils.LocalNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

private sealed interface QuizAppState {
    object Loading : QuizAppState

    object UpdatingDataBase : QuizAppState

    object UpdateError : QuizAppState

    object Loaded : QuizAppState
}

@Composable
fun QuizApp() {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val quizGameViewModel = hiltViewModel<QuizGameViewModel>()
    var userPreferences by remember {
        mutableStateOf<UserPreferences?>(null)
    }
    var quizAppState by remember {
        mutableStateOf<QuizAppState>(
            QuizAppState.Loading
        )
    }

    val userPreferencesFlow = mainViewModel.userPreferencesFlow

    LaunchedEffect(key1 = userPreferences){
        userPreferencesFlow.collectLatest {
            userPreferences = it
        }
    }

    if (quizAppState is QuizAppState.Loading && userPreferences != null) {

        val today = LocalDate.now()
        val lastUpdateDate = (userPreferences!!.lastDataUpdate ?: LocalDate.now())
        val daysAfterUpdate = lastUpdateDate.plusDays(30)
        if (userPreferences!!.isFirstLaunch || today.isAfter(daysAfterUpdate)) {
            quizAppState = QuizAppState.UpdatingDataBase

            mainViewModel.updateDatabase { success ->
                quizAppState = if (success) QuizAppState.Loaded else QuizAppState.UpdateError
            }

        } else if(userPreferences?.isFirstLaunch == false && quizAppState !is QuizAppState.Loaded) {
            quizAppState = QuizAppState.Loaded
        }
    }


    when (quizAppState) {
        is QuizAppState.Loading ->
            SplashLoadingScreen()


        is QuizAppState.UpdatingDataBase ->
            SplashLoadingScreen(loadingMessage = stringResource(R.string.msg_updating_database))

        is QuizAppState.UpdateError ->
            ErrorScreen(message = stringResource(R.string.msg_connection_error)) {
                Button(onClick = { quizAppState = QuizAppState.Loading }) {
                    Text(text = stringResource(R.string.try_again))
                }
            }

        is QuizAppState.Loaded -> {
            FarmaQuizTheme(
                darkTheme = when(userPreferences!!.themeMode){
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    else -> isSystemInDarkTheme()
                },
                dynamicColor = userPreferences!!.themeMode == ThemeMode.DYNAMIC
            ) {
                val navHostController = LocalNavController.current
                val navBackStack by navHostController.currentBackStackEntryAsState()
                val allScreens = remember { allTabScreens }
                val currentScreen =
                    allScreens.find { it.route == navBackStack?.destination?.route } ?: Home
                var showAppBar by remember { mutableStateOf(true) }

                 if (userPreferences?.isFirstLaunch == true) {
                    WelcomeScreen(
                        onUserPassWelcomeScreen = mainViewModel::onFinishWelcomeScreen
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
                                mainViewModel = mainViewModel
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun SplashLoadingScreen(
    modifier: Modifier = Modifier,
    loadingMessage: String? = null,
) {
    FarmaQuizTheme(darkTheme = isSystemInDarkTheme()) {
        Surface {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.farma_quiz_logo),
                    contentDescription = null
                )

                if (!loadingMessage.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = loadingMessage,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
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

fun NavHostController.navigateSingleTopWithStringArg(route: String, arg: String) {
    this.navigate(route = "$route/$arg") {
        launchSingleTop = true
    }
}
