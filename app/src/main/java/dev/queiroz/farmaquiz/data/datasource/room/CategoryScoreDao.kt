package dev.queiroz.farmaquiz.data.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.queiroz.farmaquiz.data.TableNames.CATEGORY_SCORE
import dev.queiroz.farmaquiz.model.CategoryScore
import dev.queiroz.farmaquiz.model.CategoryWithCategoryScore
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryScoreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(categoryScore: CategoryScore)

    @Update
    suspend fun update(categoryScore: CategoryScore)

    @Query("SELECT * FROM $CATEGORY_SCORE")
    fun getAll(): Flow<List<CategoryWithCategoryScore>>

    @Query("SELECT * FROM $CATEGORY_SCORE WHERE categoryId = :categoryId")
    fun getByCategory(categoryId: String): CategoryScore?
}