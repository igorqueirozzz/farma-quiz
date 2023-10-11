package dev.queiroz.farmaquiz.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import dev.queiroz.farmaquiz.R

interface QuizDestination {
    val route: String
    val name: String
    val icon: ImageVector
}

object Home : QuizDestination {
    override val route: String = "home"
    override val name: String = "Home"
    override val icon: ImageVector = Icons.Rounded.Home
}

object Statistics : QuizDestination {
    override val route: String = "statistics"
    override val name: String = "Estatísticas"
    override val icon: ImageVector = Icons.Rounded.PieChart
}

object QuizGame: QuizDestination {
    override val route: String = "quiz_game"
    const val routeWithArg: String = "quiz_game/{categoryId}"
    override val icon: ImageVector = Icons.Rounded.Games
    override val name: String = "Quiz Game"
}

val allTabScreens = listOf(Home, Statistics)