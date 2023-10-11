package dev.queiroz.farmaquiz.ui.screen.quizgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.data.repository.QuestionRepository
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.CategoryScore
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import dev.queiroz.farmaquiz.model.enum.Difficult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface QuizGameState {
    data class Gaming(
        val category: Category,
        val questions: Flow<List<QuestionWithAnswers>>,
        val selectedAnswer: Answer? = null
    ) : QuizGameState {
        fun update(
            categoryNew: Category? = null,
            questionsNew: Flow<List<QuestionWithAnswers>>? = null,
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

}

@HiltViewModel
class QuizGameViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryScoreRepository: CategoryScoreRepository
) : ViewModel() {

    private var questionsWithAnswers: Flow<List<QuestionWithAnswers>> =
        MutableStateFlow(emptyList())

    private val _gameState = MutableStateFlow<QuizGameState>(QuizGameState.Loading)
    val gameState = _gameState.asStateFlow()

    private var answeredQuestions: MutableList<Question> = mutableListOf()
    private var score: Int = 0

    private var categoryScore: CategoryScore? = null
    private var category: Category? = null

    fun loadQuestionByCategory(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            questionsWithAnswers =
                questionRepository.getQuestionsWithAnswersByCategoryStream(categoryId)
            category = categoryRepository.findById(categoryId)
            _gameState.value = QuizGameState.Gaming(
                category = category!!,
                questions = questionsWithAnswers
            )
            categoryScore = categoryScoreRepository.getByCategory(categoryId = categoryId)
        }
    }

    fun onSelectAnswer(answer: Answer?, question: Question?) {
        _gameState.value = (gameState.value as QuizGameState.Gaming).update(
            selectedAnswerNew = answer
        )
        if (answer?.isCorrect == true) {
            question?.alreadyAnswered = true
            answeredQuestions.add(question!!)

            score += when (question.difficult) {
                Difficult.easy -> 5
                Difficult.medium -> 10
                Difficult.hard -> 20
            }
        }
    }

    fun onFinishGame() {
        viewModelScope.launch {
            questionRepository.updateAll(answeredQuestions)
            if (categoryScore != null) {
                categoryScore!!.score += score
                categoryScoreRepository.update(categoryScore = categoryScore!!)
            } else {
                categoryScoreRepository.insert(
                    categoryScore = CategoryScore(
                        categoryId = category!!.id,
                        score = score
                    )
                )
            }
        }
        val totalOfQuestions = 20
        _gameState.value = QuizGameState.Finished(
            message = "Fim de jogo.",
            answeredQuestions = answeredQuestions.size,
            totalOfQuestions = totalOfQuestions,
            score = score
        )
    }

    fun resetGame() {
        _gameState.value = QuizGameState.Loading
        score = 0
        answeredQuestions.clear()
    }
}