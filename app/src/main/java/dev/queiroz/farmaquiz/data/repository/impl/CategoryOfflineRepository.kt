package dev.queiroz.farmaquiz.data.repository.impl

import dev.queiroz.farmaquiz.data.datasource.room.CategoryDao
import dev.queiroz.farmaquiz.data.repository.CategoryRepository
import dev.queiroz.farmaquiz.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryOfflineRepository(private val categoryDao: CategoryDao) : CategoryRepository {
    override suspend fun insert(category: Category) = categoryDao.insert(category = category)

    override suspend fun update(category: Category) = categoryDao.update(category = category)

    override suspend fun delete(category: Category) = categoryDao.delete(category = category)

    override fun getAllStream(): Flow<List<Category>> = categoryDao.getAll()
    override fun findById(categoryId: String): Category =
        categoryDao.findById(categoryId = categoryId)
}