package dev.queiroz.farmaquiz.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Games
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material.icons.rounded.Settings
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

object Settings : QuizDestination {
    override val route: String = "settings"
    override val name: String = "Configurações"
    override val icon: ImageVector = Icons.Rounded.Settings
}

object About : QuizDestination{
    override val route: String = "about"
    override val name: String = "Sobre"
    override val icon: ImageVector = Icons.Rounded.Info
}

val allTabScreens = listOf(Home, Statistics, Settings, About)