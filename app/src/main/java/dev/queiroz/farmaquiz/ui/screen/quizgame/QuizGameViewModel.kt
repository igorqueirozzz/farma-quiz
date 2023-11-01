package dev.queiroz.farmaquiz.ui.screen.quizgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.data.repository.QuestionRepository
import dev.queiroz.farmaquiz.model.Answer
import dev.queiroz.farmaquiz.model.Category
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import dev.queiroz.farmaquiz.model.enum.Difficult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface QuizGameState {
    data class Gaming(
        val category: Category,
        val questions: List<QuestionWithAnswers>,
        val selectedAnswer: Answer? = null
    ) : QuizGameState {
        fun update(
            categoryNew: Category? = null,
            questionsNew: List<QuestionWithAnswers>? = null,
            selectedAnswerNew: Answer? = null
        ): Gaming {
            return Gaming(
                category = categoryNew ?: this.category,
                questions = questionsNew ?: this.questions,
                selectedAnswer = selectedAnswerNew
            )
        }
    }

    data class RandomGaming(
        val questions: List<QuestionWithAnswers>, val selectedAnswer: Answer? = null
    ) : QuizGameState {
        fun update(
            questionsNew: List<QuestionWithAnswers>? = null, selectedAnswerNew: Answer? = null
        ): RandomGaming {
            return RandomGaming(
                questions = questionsNew ?: this.questions, selectedAnswer = selectedAnswerNew
            )
        }
    }

    object Loading : QuizGameState

    data class Finished(
        val message: String, val answeredQuestions: Int, val totalOfQuestions: Int, val score: Int
    ) : QuizGameState

}

@HiltViewModel
class QuizGameViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryScoreRepository: CategoryScoreRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow<QuizGameState>(QuizGameState.Loading)
    val gameState = _gameState.asStateFlow()

    private var answeredQuestions: MutableList<Question> = mutableListOf()
    private var score: Int = 0

    private var category: Category? = null

    fun loadQuestionByCategory(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {

            category = categoryRepository.findById(categoryId)

            val dbQuestions = questionRepository.getQuestionsWithAnswersByCategory(categoryId)


            val questions =
                dbQuestions.filter { !it.question.alreadyAnswered }.take(20).shuffled().toMutableList()

            _gameState.value =
                QuizGameState.Gaming(category = category!!, questions = questions.also {
                    if (it.size < 20) {
                        it.addAll(dbQuestions.filter { filter -> filter.question.alreadyAnswered }
                            .take(20 - it.size))
                    }
                })

        }
    }

    fun loadQuestionRandomly() {
        viewModelScope.launch(Dispatchers.IO) {
            val dbQuestions = questionRepository.getAllQuestionsWithAnswers()

            val questions =
                dbQuestions.filter { !it.question.alreadyAnswered }.take(20).toMutableList()

            _gameState.value = QuizGameState.RandomGaming(
                questions = questions.also {
                    if (it.size < 20) {
                        it.addAll(dbQuestions.filter { filter -> filter.question.alreadyAnswered }
                            .take(20 - it.size))
                    }
                })
        }
    }

    fun onSelectAnswer(answer: Answer?, question: Question?) {
        _gameState.value =
            if (gameState.value is QuizGameState.Gaming) (gameState.value as QuizGameState.Gaming).update(
                selectedAnswerNew = answer
            ) else (gameState.value as QuizGameState.RandomGaming).update(
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
        viewModelScope.launch(Dispatchers.IO) {
            questionRepository.updateAll(answeredQuestions)

            if (_gameState.value is QuizGameState.Gaming) {

                categoryScoreRepository.updateOrCreateByCategoryId(
                    categoryId = category!!.id, score = score
                )

            } else {
                answeredQuestions.forEach { question ->
                    categoryScoreRepository.updateOrCreateByCategoryId(
                        categoryId = question.categoryId, score = when (question.difficult) {
                            Difficult.easy -> 5
                            Difficult.medium -> 10
                            Difficult.hard -> 20
                        }
                    )
                }
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