package dev.queiroz.farmaquiz.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.queiroz.farmaquiz.data.TableNames

@Entity(tableName = TableNames.ANSWER)
data class Answer(
    @PrimaryKey(autoGenerate = false) var id: String,
    var text: String,
    var isCorrect: Boolean,
    var questionId: String
)