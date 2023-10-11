package dev.queiroz.farmaquiz.data.repository.impl

import dev.queiroz.farmaquiz.data.datasource.room.PlayerDao
import dev.queiroz.farmaquiz.data.repository.PlayerRepository
import dev.queiroz.farmaquiz.model.Player
import kotlinx.coroutines.flow.Flow

class PlayerOfflineRepository (private val playerDao: PlayerDao): PlayerRepository {
    override suspend fun insert(player: Player) = playerDao.insert(player = player)

    override suspend fun update(player: Player) = playerDao.update(player = player)

    override fun getPlayerStream(): Flow<Player> = playerDao.getPlayer()
}