package com.omarzg94.mytvshows.repository

import com.omarzg94.mytvshows.data.model.Show
import com.omarzg94.mytvshows.data.network.TvMazeService
import javax.inject.Inject

class ShowRepository @Inject constructor(
    private val tvMazeService: TvMazeService
) {
    suspend fun getSchedule(): List<Show> {
        return tvMazeService.getSchedule()
    }

    suspend fun getShowDetails(id: Int): Show {
        return tvMazeService.getShowDetails(id)
    }
}