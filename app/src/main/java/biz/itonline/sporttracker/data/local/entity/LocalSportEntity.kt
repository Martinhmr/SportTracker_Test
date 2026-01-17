package biz.itonline.sporttracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport_records")
data class LocalSportEntity(
    @PrimaryKey val id: String,
    val name: String,
    val place: String,
    val durationMinutes: Int,
    val timestamp: Long
)