package dev.queiroz.farmaquiz.data.repository.impl

import dev.queiroz.farmaquiz.data.datasource.room.CategoryScoreDao
import dev.queiroz.farmaquiz.data.repository.CategoryScoreRepository
import dev.queiroz.farmaquiz.model.CategoryScore
import dev.queiroz.farmaquiz.model.CategoryWithCategoryScore
import kotlinx.coroutines.flow.Flow

class CategoryScoreOfflineRepository(private val categoryScoreDao: CategoryScoreDao) :
    CategoryScoreRepository {
    override suspend fun insert(categoryScore: CategoryScore) =
        categoryScoreDao.insert(categoryScore = categoryScore)

    override suspend fun update(categoryScore: CategoryScore) =
        categoryScoreDao.update(categoryScore = categoryScore)

    override suspend fun updateOrCreateByCategoryId(categoryId: String, score: Int) {
        val categoryScore = categoryScoreDao.getByCategory(categoryId = categoryId)
        if (categoryScore != null) {
            categoryScore.score += score
            categoryScoreDao.update(categoryScore = categoryScore)
        } else {
            categoryScoreDao.insert(
                categoryScore = CategoryScore(
                    categoryId = categoryId,
                    score = score
                )
            )
        }
    }

    override fun getAllStream(): Flow<List<CategoryWithCategoryScore>> = categoryScoreDao.getAll()

    override fun getByCategory(categoryId: String): CategoryScore? =
        categoryScoreDao.getByCategory(categoryId = categoryId)
}