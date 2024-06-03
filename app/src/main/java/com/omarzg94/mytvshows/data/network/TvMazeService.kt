package com.omarzg94.mytvshows.data.network

import com.omarzg94.mytvshows.data.model.Episode
import com.omarzg94.mytvshows.data.model.Show
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TvMazeService {
    @GET("schedule?country=US")
    suspend fun getSchedule(): Response<List<Episode>>

    @GET("shows/{id}")
    suspend fun getShowDetails(@Path("id") id: Int): Response<Show>
}