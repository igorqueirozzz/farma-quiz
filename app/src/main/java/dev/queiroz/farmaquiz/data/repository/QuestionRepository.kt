package dev.queiroz.farmaquiz.data.repository

import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun insert(question: Question)

    suspend fun insertAll(questions: List<Question>)

    suspend fun update(question: Question)

    suspend fun updateAll(questions: List<Question>)

    suspend fun delete(question: Question)

    fun getAllStream(): Flow<List<Question>>

    fun getQuestionsWithAnswersByCategoryStream(categoryId: String): Flow<List<QuestionWithAnswers>>
}