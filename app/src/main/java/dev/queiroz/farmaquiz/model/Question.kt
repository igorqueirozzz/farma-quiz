package dev.queiroz.farmaquiz.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.queiroz.farmaquiz.data.TableNames
import dev.queiroz.farmaquiz.model.enum.Difficult

@Entity(tableName = TableNames.QUESTION)
data class Question(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var question: String = "",
    var categoryId: String = "",
    var alreadyAnswered: Boolean = false,
    var imageResource: String? = null,
    var explication: String = "",
    var difficult: Difficult = Difficult.easy
)