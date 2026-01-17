package biz.itonline.sporttracker.di

import biz.itonline.sporttracker.data.repository.SportRepositoryImpl
import biz.itonline.sporttracker.domain.repository.SportRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSportRepository(
        sportRepositoryImpl: SportRepositoryImpl
    ): SportRepository
}