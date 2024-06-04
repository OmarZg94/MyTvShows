package com.omarzg94.mytvshows.data.network

import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.data.model.Show
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvMazeService {
    @GET("schedule?country=US")
    suspend fun getSchedule(@Query(value = "date", encoded = true) date: String): Response<List<Episode>>
}