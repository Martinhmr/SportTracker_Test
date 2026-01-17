package biz.itonline.sporttracker.domain.repository

import biz.itonline.sporttracker.domain.model.SportRecord
import kotlinx.coroutines.flow.Flow

interface SportRepository {
    // Vrátí stream všech záznamů (jak lokálních, tak vzdálených)
    fun getAllSports(): Flow<List<SportRecord>>

    // Uloží záznam podle toho, co má v sobě nastaveno (Local/Remote)
    suspend fun saveSport(record: SportRecord)

    suspend fun deleteSport(record: SportRecord)
}