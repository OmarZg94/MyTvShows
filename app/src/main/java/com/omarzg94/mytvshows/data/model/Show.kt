package com.omarzg94.mytvshows.data.model

data class Show(
    val id: Int,
    val name: String,
    val image: Image?,
    val summary: String?,
    val schedule: Schedule,
    val network: ShowNetwork?
)