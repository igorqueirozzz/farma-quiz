package dev.queiroz.farmaquiz.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.queiroz.farmaquiz.data.datasource.dummy.CategoriesDummy
import dev.queiroz.farmaquiz.ui.Home
import dev.queiroz.farmaquiz.ui.QuizGame
import dev.queiroz.farmaquiz.ui.Statistics
import dev.queiroz.farmaquiz.ui.navigateSingleTop
import dev.queiroz.farmaquiz.ui.navigateSingleTopWithStringArg
import dev.queiroz.farmaquiz.ui.screen.home.HomeScreen
import dev.queiroz.farmaquiz.ui.screen.home.HomeViewModel
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizGameViewModel
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizScreen

@Composable
fun QuizNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
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
            val state by homeViewModel.state.collectAsState()
            HomeScreen(
                state = state,
                onCategorySelected = {
                    navController.navigateSingleTopWithStringArg(route = QuizGame.route, arg = it.id)
                })
        }

        composable(route = Statistics.route) {
            onRequestChangeAppBar(true)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(
                        Statistics.name
                    ), contentAlignment = Alignment.Center
            ) {
                Text(text = "TODO SCREEN")
            }
        }

        composable(route = QuizGame.routeWithArg) {
            onRequestChangeAppBar(false)
            val state by quizGameViewModel.gameState.collectAsState()
            QuizScreen(
                categoryId = it.arguments?.getString("categoryId") ?: "",
                state = state,
                onSelectAnswer = quizGameViewModel::onSelectAnswer,
                onResetGame = quizGameViewModel::resetGame,
                onLoadQuestionByCategory = quizGameViewModel::loadQuestionByCategory,
                onFinishGame = quizGameViewModel::onFinishGame,
                onNavigateBack = {
                    navController.popBackStack(route = Home.route, false)
                    quizGameViewModel.resetGame()
                },

                )
        }
    }
}