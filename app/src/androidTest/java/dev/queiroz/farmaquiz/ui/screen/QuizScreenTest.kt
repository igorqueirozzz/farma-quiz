package dev.queiroz.farmaquiz.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenGaming
import dev.queiroz.farmaquiz.constants.TestTags.quizScreenLoading
import dev.queiroz.farmaquiz.data.datasource.dummy.CategoriesDummy
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizGameState
import dev.queiroz.farmaquiz.ui.screen.quizgame.QuizScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class QuizScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val category = CategoriesDummy.categories.first()

    @Test
    fun quizScreen_displays_loading() {
        composeTestRule
            .setContent {
                val state = QuizGameState.Loading
                QuizScreen(
                    category = category,
                    onNavigateBack = { },
                    state = state,
                    onLoadQuestionByCategory = {},
                    onSelectAnswer = { _, _ -> },
                    onFinishGame = {},
                    onResetGame = {})
            }
        composeTestRule
            .onNodeWithTag(quizScreenLoading)
            .assertIsDisplayed()
    }

    @Test
    fun quizScreen_request_load_questions_and_displays_gaming() {
        var state: QuizGameState by
        mutableStateOf(QuizGameState.Loading)
        composeTestRule
            .setContent {
                QuizScreen(
                    category = category,
                    onNavigateBack = {},
                    state = state,
                    onLoadQuestionByCategory = {
                        Thread.sleep(100)
                        state = QuizGameState.Gaming(
                            category = it,
                            questions = CategoriesDummy.questions.filter { q -> q.categoryId == category.id }
                        )
                    },
                    onSelectAnswer = { _, _ -> },
                    onFinishGame = {},
                    onResetGame = {})
            }

        composeTestRule
            .onNodeWithTag(quizScreenGaming)
            .assertIsDisplayed()
    }
}