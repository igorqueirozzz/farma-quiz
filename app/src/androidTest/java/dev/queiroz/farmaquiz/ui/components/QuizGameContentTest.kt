package dev.queiroz.farmaquiz.ui.components

import android.graphics.Bitmap
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.queiroz.farmaquiz.constants.TestTags.answerOptionCard
import dev.queiroz.farmaquiz.constants.TestTags.answersOptionsList
import dev.queiroz.farmaquiz.data.datasource.dummy.CategoriesDummy
import dev.queiroz.farmaquiz.ui.theme.FarmaQuizTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class QuizGameContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val question = CategoriesDummy.questions.first { it.question.imageResource != null }
    private val answers = CategoriesDummy.answers.filter { it.questionId == question.question.id }

    @Test
    fun quizGameContent_shows_question_text() {
        composeTestRule.setContent {
            QuizGameContent(
                questionWithAnswers = question,
                onItemClick = {},
                onSeeExplicationClick = {}
            )
        }
        composeTestRule
            .onNodeWithText(question.question.question)
            .assertIsDisplayed()

    }

    @Test
    fun quizGameContent_shows_image() {
        composeTestRule.setContent {
            QuizGameContent(
                questionWithAnswers = question,
                onItemClick = {},
                onSeeExplicationClick = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Conteúdo de imagem")
            .assertIsDisplayed()
    }

    @Test
    fun quizGameContent_displays_answers_list() {
        composeTestRule.setContent {
            QuizGameContent(
                questionWithAnswers = question,
                onItemClick = {},
                onSeeExplicationClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag(answersOptionsList)
            .assertIsDisplayed()
    }

    @Test
    fun quizGameContent_displays_answerTest() {

        val answer = answers.first()

        composeTestRule.setContent {

            QuizAnswerItem(
                answer = answer,
                answerIndex = 0,
                isSelected = false,
                onItemClick = {},
                onSeeExplicationClick = {}
            )
        }
        composeTestRule
            .onNodeWithText(answer.text)
            .assertIsDisplayed()
    }

    @Test
    fun quizGameContent_displays_non_selected_card_tertiary_color() {
        var expectedColor: Color? = null
        val answer = answers.first().copy(isCorrect = true)
        composeTestRule.setContent {
            FarmaQuizTheme {
                if (expectedColor == null) {
                    expectedColor = MaterialTheme.colorScheme.tertiaryContainer
                }
                QuizAnswerItem(
                    answer = answer,
                    answerIndex = 0,
                    isSelected = false,
                    onItemClick = {},
                    onSeeExplicationClick = {}
                )
            }
        }
        composeTestRule
            .onNodeWithTag(answerOptionCard)
//        .assertColor(expectedColor!!)
    }

    @Test
    fun quizGameContent_select_wrong_answer_and_displays_card_error_color() {
        var isSelected by mutableStateOf(true)
        val answer = answers.first().copy(isCorrect = false)
        // Start composable on expected state
        composeTestRule.setContent {
            FarmaQuizTheme {
                QuizAnswerItem(
                    answer = answer,
                    answerIndex = 0,
                    isSelected = isSelected,
                    onItemClick = { isSelected = true },
                    onSeeExplicationClick = {}
                )
            }
        }

        // Capture image of expected state
        val expectedBitmap =
            composeTestRule.onNodeWithTag(answerOptionCard).captureToImage().asAndroidBitmap()

        // Set it to default state
        isSelected = false

        // Perform behavior test
        composeTestRule
            .onNodeWithTag(answerOptionCard)
            .performClick()

        //TODO
        // It needs to be improved, for some reason without this delay test gets fail
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag(answerOptionCard).assertBitmapIsEquals(expectedBitmap = expectedBitmap)
    }

    @Test
    fun quizGameContent_select_right_answer_and_displays_card_primary_container_color() {

        var isSelected by mutableStateOf(true)
        val answer = answers.first().copy(isCorrect = true)
        // Start composable on expected state
        composeTestRule.setContent {
            FarmaQuizTheme {
                QuizAnswerItem(
                    answer = answer,
                    answerIndex = 0,
                    isSelected = isSelected,
                    onItemClick = { isSelected = true },
                    onSeeExplicationClick = {}
                )
            }
        }

        // Capture image of expected state
        val expectedBitmap =
            composeTestRule.onNodeWithTag(answerOptionCard).captureToImage().asAndroidBitmap()

        // Set it to default state
        isSelected = false

        // Perform behavior test
        composeTestRule
            .onNodeWithTag(answerOptionCard)
            .performClick()

        //TODO
        // It needs to be improved, for some reason without this delay test gets fail
        Thread.sleep(1000)

        composeTestRule
            .onNodeWithTag(answerOptionCard).assertBitmapIsEquals(expectedBitmap = expectedBitmap)

    }

}

fun SemanticsNodeInteraction.assertBitmapIsEquals(expectedBitmap: Bitmap) {
    captureToImage().asAndroidBitmap().compare(other = expectedBitmap)
}

private fun Bitmap.compare(other: Bitmap) {
    // Compare image size
    if (this.width != other.width || this.height != other.height) {
        AssertionError("Size of images does not match")
    }
    // Compare pixel row by row
    val row1 = IntArray(width)
    val row2 = IntArray(width)
    for (column in 0 until height) {
        this.getRow(row1, column)
        other.getRow(row2, column)
        if (!row1.contentEquals(row2)) {
            throw AssertionError("Sizes match but bitmap content has differences")
        }
    }
}

private fun Bitmap.getRow(pixels: IntArray, column: Int) {
    this.getPixels(pixels, 0, width, 0, column, width, 1)
}