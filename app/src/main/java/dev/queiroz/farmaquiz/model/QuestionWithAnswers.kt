package dev.queiroz.farmaquiz.model

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithAnswers(
    @Embedded
    val question: Question,
    @Relation(parentColumn = "id", entityColumn = "questionId")
    val answers: List<Answer>
)