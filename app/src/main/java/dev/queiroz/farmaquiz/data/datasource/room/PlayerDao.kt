package dev.queiroz.farmaquiz.data.datasource.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.queiroz.farmaquiz.data.TableNames.PLAYER
import dev.queiroz.farmaquiz.model.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: Player)

    @Update
    suspend fun update(player: Player)

    @Query("SELECT * FROM $PLAYER")
    fun getPlayer(): Flow<Player>
}