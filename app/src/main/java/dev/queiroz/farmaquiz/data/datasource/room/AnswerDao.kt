package dev.queiroz.farmaquiz.data.datasource.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.queiroz.farmaquiz.data.TableNames.ANSWER
import dev.queiroz.farmaquiz.model.Answer
import kotlinx.coroutines.flow.Flow

@Dao
interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(answer: Answer)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(answers: List<Answer>)

    @Update
    suspend fun update(answer: Answer)

    @Update
    suspend fun updateAll(answers: List<Answer>)

    @Delete
    suspend fun delete(answer: Answer)

    @Query("SELECT * FROM $ANSWER")
    fun getAll(): Flow<Answer>

    @Query("SELECT * FROM $ANSWER WHERE questionId = :questionId")
    fun getAllByQuestion(questionId: String): Flow<Answer>
}