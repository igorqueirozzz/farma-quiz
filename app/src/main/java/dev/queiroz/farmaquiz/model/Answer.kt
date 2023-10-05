package dev.queiroz.farmaquiz.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.queiroz.farmaquiz.data.TableNames

@Entity(tableName = TableNames.answer)
data class Answer(@PrimaryKey(autoGenerate = false) val id: String, val text: String, val isCorrect: Boolean, val questionId: String)