package dev.queiroz.farmaquiz.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithCategoryScore(
    @Embedded
    val categoryScore: CategoryScore,
    @Relation(parentColumn = "categoryId", entityColumn = "id")
    val category: Category
)