package com.omarzg94.mytvshows.data.network

import com.omarzg94.mytvshows.data.model.Show
import retrofit2.http.GET
import retrofit2.http.Path

interface TvMazeService {
    @GET("schedule?country=US")
    suspend fun getSchedule(): List<Show>

    @GET("shows/{id}")
    suspend fun getShowDetails(@Path("id") id: Int): Show
}