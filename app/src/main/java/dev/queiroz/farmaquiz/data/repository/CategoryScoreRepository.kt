package dev.queiroz.farmaquiz.data.repository

import dev.queiroz.farmaquiz.model.CategoryScore
import dev.queiroz.farmaquiz.model.CategoryWithCategoryScore
import kotlinx.coroutines.flow.Flow

interface CategoryScoreRepository {
    suspend fun insert(categoryScore: CategoryScore)
    suspend fun update(categoryScore: CategoryScore)

    suspend fun updateOrCreateByCategoryId(categoryId: String, score: Int)

    fun getAllStream(): Flow<List<CategoryWithCategoryScore>>

    fun getByCategory(categoryId: String): CategoryScore?
}