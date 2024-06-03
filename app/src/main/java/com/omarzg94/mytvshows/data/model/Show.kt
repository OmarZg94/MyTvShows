package com.omarzg94.mytvshows.data.model

data class Show(
    val id: Int,
    val name: String,
    val genres: ArrayList<String>?,
    val premiered: String?,
    val ended: String?,
    val image: Image?,
    val summary: String?,
    val rating: Rating?,
    val schedule: Schedule,
    val network: ShowNetwork?
)