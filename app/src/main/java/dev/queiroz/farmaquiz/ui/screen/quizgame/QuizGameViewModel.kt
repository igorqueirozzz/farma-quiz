package dev.queiroz.farmaquiz.ui.screen.quizgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.queiroz.farmaquiz.data.CategoriesDummy
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.enum.Difficult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface QuizGameState {
    data class Gaming(
        val category: Category,
        val questions: List<Question>,
        val selectedAnswer: Answer? = null
    ) : QuizGameState {
        fun update(
            categoryNew: Category? = null,
            questionsNew: List<Question>? = null,
            selectedAnswerNew: Answer? = null
        ): Gaming {
            return Gaming(
                category = categoryNew ?: this.category,
                questions = questionsNew ?: this.questions,
                selectedAnswer = selectedAnswerNew
            )
        }
    }

    object Loading : QuizGameState

    data class Finished(
        val message: String,
        val answeredQuestions: Int,
        val totalOfQuestions: Int,
        val score: Int
    ) : QuizGameState

    fun getAsGaming(): Gaming = this@QuizGameState as Gaming
    fun getAsFinished(): Finished = this@QuizGameState as Finished
}

@HiltViewModel
class QuizGameViewModel @Inject constructor() : ViewModel() {
    private val _gameState = MutableStateFlow<QuizGameState>(QuizGameState.Loading)
    val gameState = _gameState.asStateFlow()

    private var answeredQuestions: Int = 0
    private var score: Int = 10

    fun loadQuestionByCategory(category: Category) {
        answeredQuestions = 0
        viewModelScope.launch {
            delay(1000)
            _gameState.value = QuizGameState.Gaming(
                category = category,
                questions = CategoriesDummy.questions.filter { it.categoryId == category.id }.take(3)
            )
        }
    }

    fun onSelectAnswer(answer: Answer?, question: Question?) {
        _gameState.value = (gameState.value as QuizGameState.Gaming).update(
            selectedAnswerNew = answer
        )
        if (answer?.isCorrect == true) {
            answeredQuestions++
            score += when (question?.difficult) {
                Difficult.easy -> 5
                Difficult.medium -> 10
                Difficult.hard -> 20
                else -> 0
            }
        }
    }

    fun onFinishGame() {
        val totalOfQuestions = _gameState.value.getAsGaming().questions.size
        _gameState.value = QuizGameState.Finished(
            message = "Fim de jogo.",
            answeredQuestions = answeredQuestions,
            totalOfQuestions = totalOfQuestions,
            score = score
        )
    }

    fun resetGame(){
        _gameState.value = QuizGameState.Loading
        score = 0
        answeredQuestions = 0
    }
}