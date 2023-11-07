package dev.queiroz.farmaquiz.ui.screen.quizgame

import androidx.compose.runtime.mutableStateOf
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.data.repository.QuestionRepository
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.utils.DispatcherProvider
import dev.queiroz.farmaquiz.utils.TestDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
@RunWith(MockitoJUnitRunner::class)
class QuizGameViewModelTest {
    private val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()
    private val questionRepository: QuestionRepository = mock()
    private val categoryRepository: CategoryRepository = mock()
    private val categoryScoreRepository: CategoryScoreRepository = mock()
    private lateinit var quizGameViewModel: QuizGameViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        quizGameViewModel = QuizGameViewModel(
            dispatcherProvider = dispatcherProvider,
            questionRepository = questionRepository,
            categoryRepository = categoryRepository,
            categoryScoreRepository = categoryScoreRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onLoadGame_should_loadCategory_loadQuestions_and_EmitGamingState() = runTest {
        val state = mutableStateOf<QuizGameState>(QuizGameState.Loading)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            quizGameViewModel.gameState.collectLatest { state.value = it }
        }
        quizGameViewModel.loadQuestionByCategory(categoryId = "1")
        advanceUntilIdle()
        verify(categoryRepository).findById(categoryId = "1")
        verify(questionRepository).getQuestionsWithAnswersByCategory(categoryId = "1")
        assert(state.value is QuizGameState.Gaming)
        job.cancel()
    }

    @Test
    fun onLoadRandomGame_should_loadCategory_loadQuestions_and_EmitGamingState() = runTest {
        val state = mutableStateOf<QuizGameState>(QuizGameState.Loading)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            quizGameViewModel.gameState.collectLatest { state.value = it }
        }
        quizGameViewModel.loadQuestionRandomly()
        advanceUntilIdle()
        verify(questionRepository).getAllQuestionsWithAnswers()
        assert(state.value is QuizGameState.RandomGaming)
        job.cancel()
    }

    @Test
    fun onSelectAnswer_should_update_gameState() = runTest {
        val state = mutableStateOf<QuizGameState>(QuizGameState.Loading)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            quizGameViewModel.gameState.collectLatest { state.value = it }
        }
        //Start game
        quizGameViewModel.loadQuestionByCategory(categoryId = "1")
        advanceUntilIdle()

        //Assert that there's no selected answer
        assert(state.value is QuizGameState.Gaming)
        assertEquals(null, (state.value as QuizGameState.Gaming).selectedAnswer)

        //Select a answer
        val expectedAnswer = Answer(text = "test", isCorrect = true, questionId = "1")
        quizGameViewModel.onSelectAnswer(
            answer = expectedAnswer,
            question = Question()
        )

        //Assert that state was updated with the answer
        assertEquals(expectedAnswer, (state.value as QuizGameState.Gaming).selectedAnswer)

        job.cancel()

    }

    @Test
    fun onSelectAnswer_should_update_randomGameState() = runTest {
        val state = mutableStateOf<QuizGameState>(QuizGameState.Loading)
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            quizGameViewModel.gameState.collectLatest { state.value = it }
        }
        //Start game
        quizGameViewModel.loadQuestionRandomly()
        advanceUntilIdle()

        //Assert that there's no selected answer
        assert(state.value is QuizGameState.RandomGaming)
        assertEquals(null, (state.value as QuizGameState.RandomGaming).selectedAnswer)

        //Select a answer
        val expectedAnswer = Answer(text = "test", isCorrect = true, questionId = "1")
        quizGameViewModel.onSelectAnswer(
            answer = expectedAnswer,
            question = Question()
        )
        advanceUntilIdle()
        //Assert that state was updated with the answer
        assertEquals(expectedAnswer, (state.value as QuizGameState.RandomGaming).selectedAnswer)

        job.cancel()

    }

}