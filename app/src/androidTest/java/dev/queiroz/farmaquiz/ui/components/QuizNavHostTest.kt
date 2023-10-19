package dev.queiroz.farmaquiz.ui.components

import android.content.res.Resources
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import dev.queiroz.farmaquiz.constants.TestTags
import dev.queiroz.farmaquiz.constants.TestTags.categoryCardItem
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenLoading
import dev.queiroz.farmaquiz.data.datasource.room.QuestionDao
import dev.queiroz.farmaquiz.data.repository.impl.QuestionOfflineRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import dev.queiroz.farmaquiz.ui.Home
import dev.queiroz.farmaquiz.ui.Statistics
import dev.queiroz.farmaquiz.ui.navigateSingleTop
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizGameViewModel

@RunWith(JUnit4::class)
class QuizNavHostTest {

   /* @get:Rule
    val composeTestRule = createComposeRule()
    private val resource = Resources.getSystem()
    private lateinit var quizGameViewModel: QuizGameViewModel
    private lateinit var navController: TestNavHostController

    @Before
    fun setupNavHost(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )

            QuizNavHost(navController = navController, quizGameViewModel = quizGameViewModel)
        }
    }

    @Test
    fun quizNavHost_displays_home(){
        composeTestRule
            .onNodeWithTag(Home.name)
            .assertIsDisplayed()
    }

    @Test
    fun quizNavHost_navigates_to_quiz_game(){
        composeTestRule
            .onAllNodesWithTag(categoryCardItem)
            .onFirst()
            .performClick()

      composeTestRule
          .onNodeWithTag(quizScreenLoading)
          .assertIsDisplayed()

    }*/
}