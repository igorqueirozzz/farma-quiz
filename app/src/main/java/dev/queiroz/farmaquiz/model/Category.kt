package dev.queiroz.farmaquiz.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.queiroz.farmaquiz.data.TableNames

@Entity(tableName = TableNames.category)
data class Category(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    @DrawableRes val imageId: Int
)