package dev.queiroz.farmaquiz.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    onRequestChangeAppBar: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val quizGameViewModel = hiltViewModel<QuizGameViewModel>()
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
        }

        composable(route = QuizGame.route) {
            onRequestChangeAppBar(false)
            QuizScreen(
                category = CategoriesDummy.categories.first(),
                onNavigateBack = {
                                 navController.navigateSingleTop(route = Home.route)
            }, viewModel = quizGameViewModel)
        }
    }
}