package dev.queiroz.farmaquiz.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dev.queiroz.farmaquiz.ui.Home
import dev.queiroz.farmaquiz.ui.QuizDestination
import dev.queiroz.farmaquiz.ui.allTabScreens
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

private const val TAG = "QuizAppBarTest"

@RunWith(JUnit4::class)
class QuizAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val allScreens = allTabScreens

    @Test
    fun quizAppBar_start_with_home_selected() {
        composeTestRule.setContent {
            QuizAppBar(
                allScreens = allScreens,
                currentScreen = Home,
                onTabClick = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(
                label = "Home"
            )
            .assertIsSelected()
    }

    @Test
    fun quizAppBar_select_statistics_tab() {
        composeTestRule.setContent {
            var currentScreen: QuizDestination by remember {
                mutableStateOf(Home)
            }
            QuizAppBar(
                allScreens = allTabScreens,
                currentScreen = currentScreen,
                onTabClick = { currentScreen = it }
            )
        }

        composeTestRule
            .onNodeWithContentDescription(label = "Estatísticas")
            .performClick()
            .assertIsSelected()
    }
}