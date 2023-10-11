package dev.queiroz.farmaquiz.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.queiroz.farmaquiz.data.TableNames.CATEGORY_SCORE

@Entity(
    tableName = CATEGORY_SCORE,
    indices = [
        Index(value = ["categoryId"], unique = true)
    ]
)
data class CategoryScore(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryId: String,
    var score: Int = 0
)
