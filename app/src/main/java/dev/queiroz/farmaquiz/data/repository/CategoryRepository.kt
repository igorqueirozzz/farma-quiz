package dev.queiroz.farmaquiz.data.repository

import dev.queiroz.farmaquiz.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun insert(category: Category)

    suspend fun insertAll(categories: List<Category>)

    suspend fun update(category: Category)

    suspend fun delete(category: Category)

    fun getAllStream(): Flow<List<Category>>

    fun findById(categoryId: String): Category
}