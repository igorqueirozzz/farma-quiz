package dev.queiroz.farmaquiz.data.repository.impl

import dev.queiroz.farmaquiz.data.datasource.room.QuestionDao
import dev.queiroz.farmaquiz.data.repository.QuestionRepository
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import kotlinx.coroutines.flow.Flow

class QuestionOfflineRepository(private val questionDao: QuestionDao) : QuestionRepository {
    override suspend fun insert(question: Question) = questionDao.insert(question = question)

    override suspend fun insertAll(questions: List<Question>) = questionDao.insertAll(questions = questions)

    override suspend fun update(question: Question) = questionDao.update(question = question)

    override suspend fun updateAll(questions: List<Question>) = questionDao.updateAll(questions = questions)

    override suspend fun delete(question: Question) = questionDao.delete(question = question)

    override fun getAllStream(): Flow<List<Question>> = questionDao.getAll()
    override fun getAllQuestionsWithAnswers(): List<QuestionWithAnswers> = questionDao.getAllQuestionsWithAnswers()

    override fun getQuestionsWithAnswersByCategoryStream(categoryId: String): Flow<List<QuestionWithAnswers>> =
        questionDao.getQuestionsWithAnswersByCategory(categoryId = categoryId)

    override fun getQuestionsWithAnswersByCategory(categoryId: String): List<QuestionWithAnswers> =
        questionDao.getQuestionsWithAnswersByCategoryNonStream(categoryId = categoryId)
}