package dev.queiroz.farmaquiz.data.repository

import dev.queiroz.farmaquiz.model.CategoryScore
import kotlinx.coroutines.flow.Flow

interface CategoryScoreRepository {
    suspend fun insert(categoryScore: CategoryScore)
    suspend fun update(categoryScore: CategoryScore)

    fun getAllStream(): Flow<List<CategoryScore>>

    fun getByCategory(categoryId: String): CategoryScore?
}