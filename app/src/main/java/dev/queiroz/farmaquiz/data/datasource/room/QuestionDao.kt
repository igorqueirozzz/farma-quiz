package dev.queiroz.farmaquiz.data.datasource.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.queiroz.farmaquiz.data.TableNames
import dev.queiroz.farmaquiz.data.TableNames.QUESTION
import dev.queiroz.farmaquiz.model.Question
import dev.queiroz.farmaquiz.model.QuestionWithAnswers
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(question: Question)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(questions: List<Question>)

    @Update
    suspend fun update(question: Question)

    @Update
    suspend fun updateAll(questions: List<Question>)

    @Delete
    suspend fun delete(question: Question)

    @Query("SELECT * FROM $QUESTION")
    @Transaction
    fun getAll(): Flow<List<Question>>

    @Query("SELECT *  FROM $QUESTION WHERE categoryId = :categoryId")
    @Transaction
    fun getQuestionsWithAnswersByCategory(categoryId: String): Flow<List<QuestionWithAnswers>>
}