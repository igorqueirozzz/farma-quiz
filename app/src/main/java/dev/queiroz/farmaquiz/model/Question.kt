package dev.queiroz.farmaquiz.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import dev.queiroz.farmaquiz.data.TableNames
import dev.queiroz.farmaquiz.model.enum.Difficult

@Entity(tableName = TableNames.question)
data class Question(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val question: String,
    @Relation(parentColumn = "id", entityColumn = "questionId")
    val answers: List<Answer>,
    val categoryId: String,
    val alreadyAnswered: Boolean,
    @DrawableRes
    val imageResource: Int? = null,
    val explication: String,
    val difficult: Difficult
)