package biz.itonline.sporttracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import biz.itonline.sporttracker.data.local.entity.LocalSportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SportDao {

    @Query("SELECT * FROM sport_records ORDER BY timestamp DESC")
    fun getAllSports(): Flow<List<LocalSportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSport(sport: LocalSportEntity)

    @Query("DELETE FROM sport_records WHERE id = :id")
    suspend fun deleteSportById(id: String)
}