package dev.queiroz.farmaquiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.queiroz.farmaquiz.model.CategoryWithCategoryScore
import dev.queiroz.farmaquiz.model.UserPreferences
import dev.queiroz.farmaquiz.ui.About
import dev.queiroz.farmaquiz.ui.Home
import dev.queiroz.farmaquiz.ui.QuizGame
import dev.queiroz.farmaquiz.ui.Settings
import dev.queiroz.farmaquiz.ui.Statistics
import dev.queiroz.farmaquiz.ui.navigateSingleTop
import dev.queiroz.farmaquiz.ui.navigateSingleTopWithStringArg
import dev.queiroz.farmaquiz.ui.screen.about.AboutScreen
import dev.queiroz.farmaquiz.ui.screen.home.HomeScreen
import dev.queiroz.farmaquiz.ui.screen.viewmodel.MainViewModel
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizGameViewModel
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizScreen
import dev.queiroz.farmaquiz.ui.screen.settings.SettingsScreen
import dev.queiroz.farmaquiz.ui.screen.statistics.StatisticsScreen
import dev.queiroz.farmaquiz.ui.screen.viewmodel.HomeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuizNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    quizGameViewModel: QuizGameViewModel,
    modifier: Modifier = Modifier,
    onRequestChangeAppBar: (Boolean) -> Unit = {}
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home.route
    ) {
        composable(route = Home.route) {
            onRequestChangeAppBar(true)
            var state by remember {
                mutableStateOf<HomeState>(HomeState.LoadingState)
            }
            val stateFlow = remember {
                mainViewModel.state
            }
            LaunchedEffect(key1 = stateFlow){
                stateFlow.collectLatest {
                    state = it
                }
            }

                HomeScreen(
                    state = state,
                    onCategorySelected = {
                        navController.navigateSingleTopWithStringArg(
                            route = QuizGame.route,
                            arg = it.id
                        )
                    },
                    onMiscellaneousClick = {
                        navController.navigateSingleTop(route = QuizGame.route)
                    }
                )

        }

        composable(route = Statistics.route) {
            onRequestChangeAppBar(true)
            val scoresFlow = remember {
                mainViewModel.categoriesWithScores
            }
            var scores by remember {
                mutableStateOf<List<CategoryWithCategoryScore>>(emptyList())
            }
            LaunchedEffect(key1 = scoresFlow){
                scoresFlow.collectLatest {
                    scores = it
                }
            }
            StatisticsScreen(scores = scores)
        }

        composable(route = QuizGame.routeWithArg) {
            onRequestChangeAppBar(false)
            val state by quizGameViewModel.gameState.collectAsState()
            QuizScreen(
                categoryId = it.arguments?.getString("categoryId"),
                onNavigateBack = {
                    navController.popBackStack(route = Home.route, false)
                    quizGameViewModel.resetGame()
                },
                state = state,
                onLoadQuestionByCategory = quizGameViewModel::loadQuestionByCategory,
                onLoadRandomGaming = quizGameViewModel::loadQuestionRandomly,
                onSelectAnswer = quizGameViewModel::onSelectAnswer,
                onFinishGame = quizGameViewModel::onFinishGame,
            )
        }

        composable(route = QuizGame.route) {
            onRequestChangeAppBar(false)
            val state by quizGameViewModel.gameState.collectAsState()
            QuizScreen(
                categoryId = null,
                onNavigateBack = {
                    navController.popBackStack(route = Home.route, false)
                    quizGameViewModel.resetGame()
                },
                state = state,
                onLoadQuestionByCategory = quizGameViewModel::loadQuestionByCategory,
                onLoadRandomGaming = quizGameViewModel::loadQuestionRandomly,
                onSelectAnswer = quizGameViewModel::onSelectAnswer,
                onFinishGame = quizGameViewModel::onFinishGame,
            )
        }

        composable(route = Settings.route) {
            val userPreferences by mainViewModel.userPreferencesFlow.collectAsState(initial = null)
            if (userPreferences != null) {
                SettingsScreen(
                    userPreferences = userPreferences!!,
                    onSaveUserPreferences = mainViewModel::updateUserPreferences
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        composable(route = About.route){
            AboutScreen(modifier = Modifier.fillMaxSize())
        }
    }
}