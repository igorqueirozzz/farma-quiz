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
    val name: Int
    val icon: ImageVector
}

object Home : QuizDestination {
    override val route: String = "home"
    @StringRes
    override val name: Int = R.string.home
    override val icon: ImageVector = Icons.Rounded.Home
}

object Statistics : QuizDestination {
    override val route: String = "statistics"
    @StringRes
    override val name: Int = R.string.statistics
    override val icon: ImageVector = Icons.Rounded.PieChart
}

object QuizGame: QuizDestination {
    override val route: String = "quiz_game"
    override val icon: ImageVector = Icons.Rounded.Games
    override val name: Int = R.string.quiz_game
}

val allTabScreens = listOf(Home, Statistics)