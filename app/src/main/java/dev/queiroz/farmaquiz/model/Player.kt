package dev.queiroz.farmaquiz.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.queiroz.farmaquiz.data.TableNames.PLAYER

@Entity(tableName = PLAYER)
data class Player(@PrimaryKey(autoGenerate = true) val id: Int = 0, val name: String)
