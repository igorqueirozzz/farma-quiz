package dev.queiroz.farmaquiz.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import dev.queiroz.farmaquiz.data.TableNames
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Entity(tableName = TableNames.CATEGORY)
data class Category(
    @PrimaryKey(autoGenerate = false) var id: String,
    var name: String,
    var imageName: String
) {

    var remainingQuestions: Int = 0

    constructor(id: String, name: String, imageName: String, remainingQuestions: Int) : this(
        id = id,
        name = name,
        imageName = imageName
    ) {
        this.remainingQuestions = remainingQuestions
    }
}