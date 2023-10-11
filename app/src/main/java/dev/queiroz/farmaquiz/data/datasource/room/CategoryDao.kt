package dev.queiroz.farmaquiz.data.datasource.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.queiroz.farmaquiz.data.TableNames.CATEGORY
import dev.queiroz.farmaquiz.data.TableNames.QUESTION
import dev.queiroz.farmaquiz.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT c.id, c.name, c.imageName, (SELECT COUNT(id) FROM $QUESTION q WHERE q.alreadyAnswered = 0 AND q.categoryId = c.id) as remainingQuestions  FROM $CATEGORY c")
    fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM $CATEGORY WHERE id = :categoryId")
    fun findById(categoryId: String): Category
}