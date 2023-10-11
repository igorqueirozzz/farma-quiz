package dev.queiroz.farmaquiz.data.repository

import dev.queiroz.farmaquiz.model.Answer
import kotlinx.coroutines.flow.Flow

interface AnswerRepository {

    suspend fun insert(answer: Answer)

    suspend fun insertAll(answers: List<Answer>)

    suspend fun update(answer: Answer)

    suspend fun updateAll(answers: List<Answer>)

    suspend fun delete(answer: Answer)

    fun getAllStream(): Flow<Answer>

    fun getAllByQuestionStreams(questionId: String): Flow<Answer>
}