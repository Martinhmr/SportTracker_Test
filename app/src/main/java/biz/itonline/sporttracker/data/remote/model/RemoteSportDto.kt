package biz.itonline.sporttracker.data.remote.model

import com.google.firebase.firestore.PropertyName

/**
 * Data Transfer Object pro Firestore.
 */
data class RemoteSportDto(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("place") @set:PropertyName("place")
    var place: String = "",

    @get:PropertyName("duration") @set:PropertyName("duration")
    var durationMinutes: Int = 0,

    @get:PropertyName("timestamp") @set:PropertyName("timestamp")
    var timestamp: Long = 0L
)