package dev.queiroz.farmaquiz.data.repository

import dev.queiroz.farmaquiz.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun insert(player: Player)

    suspend fun update(player: Player)
    fun getPlayerStream(): Flow<Player>
}