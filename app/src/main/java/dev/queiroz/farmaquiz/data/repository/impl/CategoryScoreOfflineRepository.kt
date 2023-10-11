package dev.queiroz.farmaquiz.data.repository.impl

import dev.queiroz.farmaquiz.data.datasource.room.CategoryScoreDao
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.model.CategoryScore
import kotlinx.coroutines.flow.Flow

class CategoryScoreOfflineRepository(private val categoryScoreDao: CategoryScoreDao) :
    CategoryScoreRepository {
    override suspend fun insert(categoryScore: CategoryScore) =
        categoryScoreDao.insert(categoryScore = categoryScore)

    override suspend fun update(categoryScore: CategoryScore) =
        categoryScoreDao.update(categoryScore = categoryScore)

    override fun getAllStream(): Flow<List<CategoryScore>> = categoryScoreDao.getAll()

    override fun getByCategory(categoryId: String): CategoryScore? =
        categoryScoreDao.getByCategory(categoryId = categoryId)
}