package dev.queiroz.farmaquiz.data.repository.impl

import dev.queiroz.farmaquiz.data.datasource.room.AnswerDao
import dev.queiroz.farmaquiz.data.repository.AnswerRepository
import dev.queiroz.farmaquiz.model.Answer
import kotlinx.coroutines.flow.Flow

class AnswerOfflineRepository(private val answerDao: AnswerDao) : AnswerRepository {
    override suspend fun insert(answer: Answer) = answerDao.insert(answer = answer)

    override suspend fun insertAll(answers: List<Answer>) = answerDao.insertAll(answers = answers)

    override suspend fun update(answer: Answer) = answerDao.update(answer = answer)

    override suspend fun updateAll(answers: List<Answer>) = answerDao.updateAll(answers = answers)

    override suspend fun delete(answer: Answer) = answerDao.delete(answer = answer)

    override fun getAllStream(): Flow<Answer> = answerDao.getAll()

    override fun getAllByQuestionStreams(questionId: String): Flow<Answer> =
        answerDao.getAllByQuestion(questionId = questionId)

}