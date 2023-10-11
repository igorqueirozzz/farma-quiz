package dev.queiroz.farmaquiz.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.queiroz.farmaquiz.constants.TestTags.backButton
import dev.queiroz.farmaquiz.constants.TestTags.skipButton
import dev.queiroz.farmaquiz.data.datasource.dummy.CategoriesDummy
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class QuizGameAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val category = CategoriesDummy.categories.first()
    private val questions = CategoriesDummy.questions.filter { it.categoryId == category.id }
    val TAG = "QuizGameAppBarTest"

    @Test
    fun quizGameAppBar_displaysBackButton() {
        composeTestRule.setContent {
            QuizGameAppBar(
                categoryName = category.name,
                currentQuestionIndex = 0,
                totalOfQuestions = questions.size,
                onBackClick = { },
                onSkipClick = { })
        }

        composeTestRule
            .onNodeWithTag(backButton)
            .assertIsDisplayed()
    }

    @Test
    fun quizGameAppBar_displays_category_name() {
        composeTestRule.setContent {
            QuizGameAppBar(
                categoryName = category.name,
                currentQuestionIndex = 0,
                totalOfQuestions = questions.size,
                onBackClick = { },
                onSkipClick = { })
        }

        composeTestRule
            .onNodeWithText(category.name)
            .assertIsDisplayed()
    }

    @Test
    fun quizGameAppBar_displays_skip_button() {
        composeTestRule.setContent {
            QuizGameAppBar(
                categoryName = category.name,
                currentQuestionIndex = 0,
                totalOfQuestions = questions.size,
                onBackClick = { },
                onSkipClick = { })
        }

        composeTestRule
            .onNodeWithTag(skipButton)
            .assertIsDisplayed()
    }

    @Test
    fun quizGameAppBar_displays_game_progress() {
        composeTestRule.setContent {
            QuizGameAppBar(
                categoryName = category.name,
                currentQuestionIndex = 0,
                totalOfQuestions = questions.size,
                onBackClick = { },
                onSkipClick = { })
        }

        composeTestRule
            .onNodeWithText(text = "0 / ${questions.size}")
            .assertIsDisplayed()
    }

    @Test
    fun quizGameAppBar_skip_button_updates_game_progress() {
        composeTestRule.setContent {
            var questionIndex by remember { mutableStateOf(0) }
            QuizGameAppBar(
                categoryName = category.name,
                currentQuestionIndex = questionIndex,
                totalOfQuestions = questions.size,
                onBackClick = { },
                onSkipClick = { questionIndex++ })
        }

        composeTestRule
            .onNodeWithTag(skipButton)
            .performClick()

        composeTestRule
            .onNodeWithText("1 / ${questions.size}")
            .assertIsDisplayed()
    }


}