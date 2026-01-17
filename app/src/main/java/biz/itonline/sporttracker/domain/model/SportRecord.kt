package biz.itonline.sporttracker.domain.model

import java.util.UUID
import java.util.Date

data class SportRecord(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val place: String,
    val durationMinutes: Int,
    val date: Date = Date(),
    val type: StorageType
)

enum class StorageType {
    LOCAL, REMOTE
}