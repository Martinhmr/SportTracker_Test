package biz.itonline.sporttracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import biz.itonline.sporttracker.data.local.dao.SportDao
import biz.itonline.sporttracker.data.local.entity.LocalSportEntity

@Database(entities = [LocalSportEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sportDao(): SportDao
}