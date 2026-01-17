package biz.itonline.sporttracker.di

import android.content.Context
import androidx.room.Room
import biz.itonline.sporttracker.data.local.AppDatabase
import biz.itonline.sporttracker.data.local.dao.SportDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sport_tracker_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideSportDao(database: AppDatabase): SportDao {
        return database.sportDao()
    }
}