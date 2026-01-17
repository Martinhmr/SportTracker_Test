package biz.itonline.sporttracker.data

import biz.itonline.sporttracker.data.local.entity.LocalSportEntity
import biz.itonline.sporttracker.data.remote.model.RemoteSportDto
import biz.itonline.sporttracker.domain.model.SportRecord
import biz.itonline.sporttracker.domain.model.StorageType
import java.util.Date

// --- Local Mappers ---

fun SportRecord.toEntity(): LocalSportEntity {
    return LocalSportEntity(
        id = this.id,
        name = this.name,
        place = this.place,
        durationMinutes = this.durationMinutes,
        timestamp = this.date.time
    )
}

fun LocalSportEntity.toDomain(): SportRecord {
    return SportRecord(
        id = this.id,
        name = this.name,
        place = this.place,
        durationMinutes = this.durationMinutes,
        date = Date(this.timestamp),
        type = StorageType.LOCAL
    )
}

// --- Remote Mappers ---

fun SportRecord.toRemoteDto(): RemoteSportDto {
    return RemoteSportDto(
        id = this.id,
        name = this.name,
        place = this.place,
        durationMinutes = this.durationMinutes,
        timestamp = this.date.time
    )
}

fun RemoteSportDto.toDomain(): SportRecord {
    return SportRecord(
        id = this.id,
        name = this.name,
        place = this.place,
        durationMinutes = this.durationMinutes,
        date = Date(this.timestamp),
        type = StorageType.REMOTE
    )
}