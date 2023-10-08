package dev.queiroz.farmaquiz.ui.components

import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.queiroz.farmaquiz.R
import dev.queiroz.farmaquiz.data.CategoriesDummy
import dev.queiroz.farmaquiz.ui.Home
import dev.queiroz.farmaquiz.ui.QuizGame
import dev.queiroz.farmaquiz.ui.Statistics
import dev.queiroz.farmaquiz.ui.navigateSingleTop
import dev.queiroz.farmaquiz.ui.screen.HomeScreen
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizGameViewModel
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizScreen

@Composable
fun QuizNavHost(
    navController: NavHostController,
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
            HomeScreen(onCategorySelected = {
                navController.navigateSingleTop(route = QuizGame.route)
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

        composable(route = QuizGame.route) {
            onRequestChangeAppBar(false)
            val state by quizGameViewModel.gameState.collectAsState()
            QuizScreen(
                category = CategoriesDummy.categories.first(),
                state = state,
                onSelectAnswer = quizGameViewModel::onSelectAnswer,
                onResetGame = quizGameViewModel::resetGame,
                onLoadQuestionByCategory = quizGameViewModel::loadQuestionByCategory,
                onFinishGame = quizGameViewModel::onFinishGame,
                onNavigateBack = {
                    navController.navigateSingleTop(route = Home.route)
                },

                )
        }
    }
}