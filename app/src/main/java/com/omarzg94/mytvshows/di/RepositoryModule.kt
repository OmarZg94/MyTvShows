package com.omarzg94.mytvshows.di

import com.omarzg94.mytvshows.data.network.TvMazeService
import com.omarzg94.mytvshows.repository.ShowRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideShowRepository(tvMazeService: TvMazeService): ShowRepository {
        return ShowRepository(tvMazeService)
    }
}